package jsonz
import jsonz.models._
import scalaz.{Failure, Success}
import org.scalacheck.{Arbitrary, Prop}

object SerializationSpec extends JsonzSpec {
  import DefaultFormats._

  "Writes arbitrary JsValue's to and from strings" ! check { js: JsValue =>
    val wrote = Jsonz.toJsonStr(js)
    val read = Jsonz.fromJsonStr[JsValue](wrote)
    read must beLike {
      case Success(s) => s must beEqualTo(js)
    }
  }

  "\"null\" should translate to a JsNull not null" ! {
    // once this passes we should be able to remove the null case in Reads[JsValue]
    JerksonJson.parse[JsValue]("null") must_== JsNull
  }.pendingUntilFixed("why is this null?")

  "array containing null" ! {
    val read = JerksonJson.parse[JsValue]("[null]")
    read must beLike {
      case JsArray(elements) =>
        elements must contain(JsNull: JsValue).only
    }
  }

  "null on a string must be a failure" ! {
    Jsonz.fromJsonStr[String]("null") must beLike {
      case Failure(failures) =>
        failures.list must contain(JsFailureStatement("not a string"))
    }
  }

  "Failure when unparseable" ! pending

}
