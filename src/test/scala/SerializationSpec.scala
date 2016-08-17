package jsonz
import jsonz.models._
import scalaz.{Failure, Success}
import org.scalacheck.{Arbitrary, Prop}
import jsonz.specs2.Specs2JsonzTestkit

object SerializationSpec extends JsonzSpec {
  import jsonz._
  import jsonz.DefaultFormats._

  "Writes arbitrary JsValue's to and from strings" ! prop { js: JsValue =>
    val wrote = Jsonz.toJsonStr(js)
    val read = Jsonz.fromJsonStr[JsValue](wrote)
    read must beSuccessful(js)
  }

  "\"null\" should translate to a JsNull not null" ! {
    JacksonJson.parse[JsValue]("null") must_== JsNull
  }

  "array containing null" ! {
    val read = JacksonJson.parse[JsValue]("[null]")
    read must beLike {
      case JsArray(elements) =>
        elements must contain(allOf(JsNull: JsValue))
    }
  }

  "null on a string must be a failure" ! {
    Jsonz.fromJsonStr[String]("null") must containFailure(JsFailureStatement("not a string"))
  }

  "Failure when unparseable" ! {
    Jsonz.fromJsonStr[String]("not valid json") must containFailure(JsFailureStatement("not valid JSON"))
  }

  "to/from bytes" ! prop { js: JsValue =>
    val wrote: Array[Byte] = Jsonz.toJsonBytes(js)
    val read = Jsonz.fromJsonBytes[JsValue](wrote)
    read must beSuccessful(js)
  }

  "to/from Input/Output streams" ! prop { js: JsValue =>
    import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

    val out = new ByteArrayOutputStream
    Jsonz.toJsonOutputStream(js, out)

    val in = new ByteArrayInputStream(out.toByteArray)
    val read = Jsonz.fromJsonInputStream[JsValue](in)

    read must beSuccessful(js)
  }

  "take a Map, turn it into bytes with jsonz and read out as a string without jsonz" in {
    val map = Map("a" -> List(1), "b" -> List(2,3))
    val bytes = Jsonz.toJsonBytes(map)
    val Some(parsed) = Jsonz.parse(bytes.map(_.toChar).mkString).toOption
    parsed must haveJsonField('a, List(1))
    parsed must haveJsonField('b, List(2,3))
  }

  "take a json object raw string, turn into bytes, and read out via jsonz" in {
    val map = """{"a":[1], "b": [2,3]}"""
    val bytes1 = map.getBytes
    val bytes2 = map.getBytes("UTF-8")
    val Some(parsed1) = Jsonz.fromJsonBytes[Map[String, List[Int]]](bytes1).toOption
    val Some(parsed2) = Jsonz.fromJsonBytes[Map[String, List[Int]]](bytes2).toOption
    parsed1 must contain(exactly("a" -> List(1), "b" -> List(2,3)))
    parsed2 must contain(exactly("a" -> List(1), "b" -> List(2,3)))
  }

  "streaming" ! pending
}
