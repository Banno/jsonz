package jsonz

// Originally from the Play Framework 2.0

import com.fasterxml.jackson.core.{JsonGenerator, JsonToken, JsonParser, Version}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.`type`._
import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.Module.SetupContext
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.annotation.tailrec

// -- Serializers.

private[jsonz] class JsValueSerializer extends JsonSerializer[JsValue] {

  def serialize(value: JsValue, json: JsonGenerator, provider: SerializerProvider) {
    value match {
      case JsNumber(v) => json.writeNumber(v.bigDecimal)
      case JsString(v) => json.writeString(v)
      case JsBoolean(v) => json.writeBoolean(v)
      case JsArray(elements) => json.writeObject(elements)
      case JsObject(values) => {
        json.writeStartObject()
        values.foreach { t =>
          json.writeFieldName(t._1)
          json.writeObject(t._2)
        }
        json.writeEndObject()
      }
      case JsNull => json.writeNull()
    }
  }
}

private[jsonz] sealed trait DeserializerContext {
  def addValue(value: JsValue): DeserializerContext
}

private[jsonz] case class ReadingList(content: List[JsValue]) extends DeserializerContext {
  override def addValue(value: JsValue): DeserializerContext = {
    ReadingList(content :+ value)
  }
}

// Context for reading an Object
private[jsonz] case class KeyRead(content: List[(String, JsValue)], fieldName: String) extends DeserializerContext {
  def addValue(value: JsValue): DeserializerContext = ReadingMap(content :+ (fieldName -> value))
}

// Context for reading one item of an Object (we already red fieldName)
private[jsonz] case class ReadingMap(content: List[(String, JsValue)]) extends DeserializerContext {

  def setField(fieldName: String) = KeyRead(content, fieldName)
  def addValue(value: JsValue): DeserializerContext = throw new ParseException("Cannot add a value on an object without a key, malformed JSON object!")

}

//@JsonCachable
private[jsonz] class JsValueDeserializer(factory: TypeFactory, klass: Class[_]) extends JsonDeserializer[Object] {
  def deserialize(jp: JsonParser, ctxt: DeserializationContext): JsValue = {
    val value = deserialize(jp, ctxt, List())

    if (!klass.isAssignableFrom(value.getClass)) {
      throw ctxt.mappingException(klass)
    }
    value
  }

  @tailrec
  final def deserialize(jp: JsonParser, ctxt: DeserializationContext, parserContext: List[DeserializerContext]): JsValue = { // idea: return a ValidationNel
    if (jp.getCurrentToken == null) {
      jp.nextToken()
    }

    val (maybeValue, nextContext) = (jp.getCurrentToken, parserContext) match {

      case (JsonToken.VALUE_NUMBER_INT, c) => (Some(JsNumber(jp.getLongValue)), c)

      case (JsonToken.VALUE_NUMBER_FLOAT, c) => (Some(JsNumber(jp.getDoubleValue)), c)

      case (JsonToken.VALUE_STRING, c) => (Some(JsString(jp.getText)), c)

      case (JsonToken.VALUE_TRUE, c) => (Some(JsBoolean(true)), c)

      case (JsonToken.VALUE_FALSE, c) => (Some(JsBoolean(false)), c)

      case (JsonToken.VALUE_NULL, c) => (Some(JsNull), c)

      case (JsonToken.START_ARRAY, c) => (None, (ReadingList(List())) +: c)

      case (JsonToken.END_ARRAY, ReadingList(content) :: stack) => (Some(JsArray(content)), stack)

      case (JsonToken.END_ARRAY, _) => throw new ParseException("We should have been reading list, something got wrong")

      case (JsonToken.START_OBJECT, c) => (None, ReadingMap(List()) +: c)

      case (JsonToken.FIELD_NAME, (c: ReadingMap) :: stack) => (None, c.setField(jp.getCurrentName) +: stack)

      case (JsonToken.FIELD_NAME, _) => throw new ParseException("We should be reading map, something got wrong")

      case (JsonToken.END_OBJECT, ReadingMap(content) :: stack) => (Some(JsObject(content)), stack)

      case (JsonToken.END_OBJECT, _) => throw new ParseException("We should have been reading an object, something got wrong")

      case _ => throw ctxt.mappingException(classOf[JsValue])
    }

    // Read ahead
    jp.nextToken()

    maybeValue match {
      case Some(v) if nextContext.isEmpty && jp.getCurrentToken == null =>
        //done, no more tokens and got a value!
        v

      case Some(v) if nextContext.isEmpty =>
        //strange, got value, but there is more tokens and have no prior context!
        throw new ParseException("Malformed JSON: Got a sequence of JsValue outside an array or an object.")

      case maybeValue =>
        val toPass = maybeValue.map { v =>
          val previous :: stack = nextContext
          (previous.addValue(v)) +: stack
        }.getOrElse(nextContext)

        deserialize(jp, ctxt, toPass)
    }

  }
}

private[jsonz] class JsonzDeserializers(classLoader: ClassLoader) extends Deserializers.Base {
  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig, beanDesc: BeanDescription) = {
    val klass = javaType.getRawClass
    if (classOf[JsValue].isAssignableFrom(klass) || klass == JsNull.getClass) {
      new JsValueDeserializer(config.getTypeFactory, klass)
    } else null
  }

}

private[jsonz] class JsonzSerializers extends Serializers.Base {
  override def findSerializer(config: SerializationConfig, javaType: JavaType, beanDesc: BeanDescription) = {
    val ser: Object = if (classOf[JsValue].isAssignableFrom(beanDesc.getBeanClass)) {
      new JsValueSerializer
    } else {
      null
    }
    ser.asInstanceOf[JsonSerializer[Object]]
  }
}



private[jsonz] object JsonzModule extends SimpleModule("Jsonz", Version.unknownVersion()) {
  protected val classLoader = Thread.currentThread().getContextClassLoader
  override def setupModule(context: SetupContext) {
    context.addDeserializers(new JsonzDeserializers(classLoader))
    context.addSerializers(new JsonzSerializers)
  }
}

private[jsonz] object JacksonJson extends Factory with Generator with Parser {

  protected val mapper = new ObjectMapper
  protected val factory = new MappingJsonFactory(mapper)

  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(JsonzModule)

  factory.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)
  factory.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
  factory.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES)
  factory.enable(JsonParser.Feature.ALLOW_COMMENTS)
  factory.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE)
}

// Originally from Jerkson.

private[jsonz] trait Factory {
  protected def mapper: ObjectMapper
  protected def factory: MappingJsonFactory
}

private[jsonz] trait Generator extends Factory {
  import java.io.{StringWriter, OutputStream, File, Writer}
  import com.fasterxml.jackson.core.JsonEncoding

  def generate[A](obj: A): String = {
    val writer = new StringWriter
    generate(obj, writer)
    writer.toString
  }

  def generate[A](obj: A, output: Writer) {
    generate(obj, factory.createGenerator(output))
  }

  def generate[A](obj: A, output: OutputStream) {
    generate(obj, factory.createGenerator(output, JsonEncoding.UTF8))
  }

  def generate[A](obj: A, output: File) {
    generate(obj, factory.createGenerator(output, JsonEncoding.UTF8))
  }

  private def generate[A](obj: A, generator: JsonGenerator) {
    generator.writeObject(obj)
    generator.close()
  }
}


private[jsonz] trait Parser extends Factory {
  import io.Source
  import java.net.URL
  import com.fasterxml.jackson.core.JsonProcessingException
  import com.fasterxml.jackson.databind.node.TreeTraversingParser
  import java.io.{EOFException, Reader, File, InputStream}

  def parse[A](input: String)(implicit mf: Manifest[A]): A = parse[A](factory.createParser(input), mf)

  def parse[A](input: InputStream)(implicit mf: Manifest[A]): A = parse[A](factory.createParser(input), mf)

  def parse[A](input: File)(implicit mf: Manifest[A]): A = parse[A](factory.createParser(input), mf)

  def parse[A](input: URL)(implicit mf: Manifest[A]): A = parse[A](factory.createParser(input), mf)

  def parse[A](input: Reader)(implicit mf: Manifest[A]): A = parse[A](factory.createParser(input), mf)

  def parse[A](input: Array[Byte])(implicit mf: Manifest[A]): A = parse[A](factory.createParser(input), mf)

  def parse[A](input: Source)(implicit mf: Manifest[A]): A = parse[A](input.mkString)

  def parse[A](input: JsonNode)(implicit mf: Manifest[A]): A = {
    val parser = new TreeTraversingParser(input, mapper)
    parse(parser, mf)
  }

  private def parse[A](parser: JsonParser, mf: Manifest[A]): A = {
    try {
      if (mf.runtimeClass == classOf[JsValue] || mf.runtimeClass == JsNull.getClass) {
        val value: A = parser.getCodec.readValue(parser, ConstructType.build(mapper.getTypeFactory, mf))
        if (value == null) JsNull.asInstanceOf[A] else value
      } else {
        parser.getCodec.readValue(parser, ConstructType.build(mapper.getTypeFactory, mf))
      }
    } catch {
      case e: JsonProcessingException => throw ParsingException(e)
      case e: EOFException => throw new ParsingException("JSON document ended unexpectedly.", e)
    }
  }
}

private[jsonz] object ConstructType {

  private val cachedTypes = scala.collection.concurrent.TrieMap.empty[Manifest[_], JavaType]

  def build(factory: TypeFactory, manifest: Manifest[_]): JavaType =
    cachedTypes.getOrElseUpdate(manifest, constructType(factory, manifest))

  private def constructType(factory: TypeFactory, manifest: Manifest[_]): JavaType = {
    if (manifest.runtimeClass.isArray) {
      ArrayType.construct(factory.constructType(manifest.runtimeClass.getComponentType), TypeBindings.emptyBindings())
    } else {
      factory.constructParametricType(
        manifest.runtimeClass,
        manifest.typeArguments.map {m => build(factory, m)}.toArray: _*)
    }
  }
}
