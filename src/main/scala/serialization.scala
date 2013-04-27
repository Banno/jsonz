package jsonz

// Originally from the Play Framework 2.0

import org.codehaus.jackson.{ JsonGenerator, JsonToken, JsonParser }
import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map._
import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.`type`.{ TypeFactory, ArrayType }

import scala.annotation.tailrec

// -- Serializers.

@JsonCachable
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
  def addValue(value: JsValue): DeserializerContext = throw new Exception("Cannot add a value on an object without a key, malformed JSON object!")

}

@JsonCachable
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

      case (JsonToken.END_ARRAY, _) => throw new RuntimeException("We should have been reading list, something got wrong")

      case (JsonToken.START_OBJECT, c) => (None, ReadingMap(List()) +: c)

      case (JsonToken.FIELD_NAME, (c: ReadingMap) :: stack) => (None, c.setField(jp.getCurrentName) +: stack)

      case (JsonToken.FIELD_NAME, _) => throw new RuntimeException("We should be reading map, something got wrong")

      case (JsonToken.END_OBJECT, ReadingMap(content) :: stack) => (Some(JsObject(content)), stack)

      case (JsonToken.END_OBJECT, _) => throw new RuntimeException("We should have been reading an object, something got wrong")

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
        throw new Exception("Malformed JSON: Got a sequence of JsValue outside an array or an object.")

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
  override def findBeanDeserializer(javaType: JavaType, config: DeserializationConfig,
    provider: DeserializerProvider, beanDesc: BeanDescription,
    property: BeanProperty) = {
    val klass = javaType.getRawClass
    if (classOf[JsValue].isAssignableFrom(klass) || klass == JsNull.getClass) {
      new JsValueDeserializer(config.getTypeFactory, klass)
    } else null
  }

}

private[jsonz] class JsonzSerializers extends Serializers.Base {
  override def findSerializer(config: SerializationConfig, javaType: JavaType, beanDesc: BeanDescription, beanProp: BeanProperty) = {
    val ser: Object = if (classOf[JsValue].isAssignableFrom(beanDesc.getBeanClass)) {
      new JsValueSerializer
    } else {
      null
    }
    ser.asInstanceOf[JsonSerializer[Object]]
  }
}

private[jsonz] object JerksonJson extends com.codahale.jerkson.Json {
  import org.codehaus.jackson.Version
  import org.codehaus.jackson.map.module.SimpleModule
  import org.codehaus.jackson.map.Module.SetupContext

  object module extends SimpleModule("Jsonz", Version.unknownVersion()) {
    override def setupModule(context: SetupContext) {
      context.addDeserializers(new JsonzDeserializers(classLoader))
      context.addSerializers(new JsonzSerializers)
    }
  }
  mapper.registerModule(module)

}
