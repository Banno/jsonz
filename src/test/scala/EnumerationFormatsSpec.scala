package jsonz
import jsonz.models._
import org.specs2.specification.Scope

object EnumerationFormatsSpec extends JsonzSpec {
  import Jsonz._
  import DefaultFormats._

  // object Animals {
  //   val Tiger = Value
  //   val Lion = Value
  //   val Swallow = Value

  // public enum Day {
  //   SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY

  "Should convert to and from scala enumerations" in new context {
    fromJson(toJson(Animals.Tiger))(AnimalsFormat) must beSuccess(Animals.Tiger)
    fromJson(JsString("swallow"))(AnimalsFormat) must beSuccess(Animals.Swallow)
    fromJson(JsString("l1on"))(AnimalsFormat) must not(beSuccess(Animals.Lion))
  }

  "convert to and from java enums" in pending
  "not care about case for java enums" in pending

  trait context extends Scope {
    implicit val AnimalsFormat: Format[Animals.Value] = scalaEnumerationFormat(Animals)
    // implicit val DaysFormat = javaEnumerationFormat[Day](Day)
  }
}
