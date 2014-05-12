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
    fromJson(JsString("nope"))(AnimalsFormat) must containFailure(
      JsFailureStatement("not one of List(Tiger, Lion, Swallow)")
    )
  }

  "convert to and from scala enumerations with overrides" in new context {
    override implicit val AnimalsFormat: Format[Animals.Value] =
      scalaEnumerationFormat(Animals, (Animals.Tiger -> "Hippo"))

    fromJson(toJson(Animals.Tiger))(AnimalsFormat) must beSuccess(Animals.Tiger)
    fromJson(toJson(Animals.Tiger))(AnimalsFormat) must beSuccess(Animals.Tiger)
    fromJson(JsString("swallow"))(AnimalsFormat) must beSuccess(Animals.Swallow)
    fromJson(JsString("l1on"))(AnimalsFormat) must not(beSuccess(Animals.Lion))
    fromJson(JsString("nope"))(AnimalsFormat) must containFailure(
      JsFailureStatement("not one of List(Tiger, Lion, Swallow)")
    )
    fromJson(toJson(Animals.Tiger))(AnimalsFormat) must beSuccess(Animals.Tiger)
    toJson(Animals.Tiger)(AnimalsFormat) === JsString("Hippo")
    fromJson(JsString("hippo"))(AnimalsFormat) must beSuccess(Animals.Tiger)
  }

  "convert to and from java enums" in new context {
    fromJson(toJson(Day.MONDAY))(DaysFormat) must beSuccess(Day.MONDAY)
    fromJson(JsString("TUESDAY"))(DaysFormat) must beSuccess(Day.TUESDAY)
    fromJson(JsString("tuesday"))(DaysFormat) must beSuccess(Day.TUESDAY)
    fromJson(JsString("nope"))(DaysFormat) must containFailure(
      JsFailureStatement("not one of List(SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY)")
    )
  }

  trait context extends Scope {
    implicit val AnimalsFormat: Format[Animals.Value] = scalaEnumerationFormat(Animals)
    implicit val DaysFormat: Format[Day] = javaEnumerationFormat[Day](Day.MONDAY)
  }
}
