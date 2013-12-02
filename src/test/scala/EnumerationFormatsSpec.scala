package jsonz
import jsonz.models._
import org.specs2.specification.Scope

object EnumerationFormatsSpec extends JsonzSpec {
  import Jsonz._
  import DefaultFormats._

  "Should convert to and from scala enumerations" in new context {
    fromJson(toJson(Animals.Tiger))(AnimalsFormat) must beSuccess(Animals.Tiger)
    fromJson(JsString("swallow"))(AnimalsFormat) must beSuccess(Animals.Swallow)
    fromJson(JsString("l1on"))(AnimalsFormat) must not(beSuccess(Animals.Lion))
  }

  "convert to and from java enums" in new context {
    fromJson(toJson(Day.MONDAY))(DaysFormat) must beSuccess(Day.MONDAY)
    fromJson(JsString("TUESDAY"))(DaysFormat) must beSuccess(Day.TUESDAY)
    fromJson(JsString("tuesday"))(DaysFormat) must beSuccess(Day.TUESDAY)
    fromJson(JsString("nope"))(DaysFormat) must containFailure(JsFailure.jsFailure("not a valid value"))
  }

  trait context extends Scope {
    implicit val AnimalsFormat: Format[Animals.Value] = scalaEnumerationFormat(Animals)
    implicit val DaysFormat: Format[Day] = javaEnumerationFormat[Day](Day.MONDAY)
  }
}
