package jsonz
import scalaz._

object Fields extends Fields

trait Fields {
  import JsFailure._

  def field[T: Reads](name: String, js: JsValue): ValidationNEL[JsFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      maybeFromJson getOrElse jsFailureValidationNel(name, "is missing")
    }
    case _ => jsFailureValidationNel("is not an object")
  }

  def fieldWithValidation[T: Reads](name: String, valid: (T) => ValidationNEL[String, T], js: JsValue): ValidationNEL[JsFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      val fromjson = maybeFromJson getOrElse jsFailureValidationNel(name, "is missing")
      fromjson.flatMap { value =>
        valid(value).fail.map(fs => jsFailureNel(name, fs)).validation
      }
    }
    case _ => jsFailureValidationNel("is not an object")
  }
}
