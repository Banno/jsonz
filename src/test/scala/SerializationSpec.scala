package jsonz
import jsonz.models._
import scalaz.{Failure, Success}
import org.scalacheck.{Arbitrary, Prop}

object SerializationSpec extends JsonzSpec {
  import DefaultFormats._

  "Writes arbitrary JsValue's to and from strings" ! check { js: JsValue =>
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

  "to/from bytes" ! check { js: JsValue =>
    val wrote: Array[Byte] = Jsonz.toJsonBytes(js)
    val read = Jsonz.fromJsonBytes[JsValue](wrote)
    read must beSuccessful(js)
  }

  "to/from Input/Output streams" ! check { js: JsValue =>
    import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

    val out = new ByteArrayOutputStream
    Jsonz.toJsonOutputStream(js, out)

    val in = new ByteArrayInputStream(out.toByteArray)
    val read = Jsonz.fromJsonInputStream[JsValue](in)

    read must beSuccessful(js)
  }


  "streaming" ! pending

}
