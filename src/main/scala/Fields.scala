package jsonz
import scalaz._

object Fields extends Fields

trait Fields {
  import JsFailure._

  def field[T: Reads](name: String, js: JsValue): ValidationNEL[JsFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      maybeFromJson.map(groupFieldJsFailures(name)) getOrElse jsFailureValidationNel(name, "is missing")
    }
    case _ => jsFailureValidationNel("is not an object")
  }

  def fieldWithValidationNel[T: Reads](name: String,
                                       valid: (T) => ValidationNEL[String, T],
                                       js: JsValue): ValidationNEL[JsFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      val fromjson = maybeFromJson.map(groupFieldJsFailures(name)) getOrElse jsFailureValidationNel(name, "is missing")
      fromjson.flatMap { value =>
        valid(value).fail.map(fs => jsFailureNel(name, fs.map(jsFailure))).validation
      }
    }
    case _ => jsFailureValidationNel("is not an object")
  }

  def fieldWithValidation[T: Reads](name: String, valid: (T) => Validation[String, T], js: JsValue): ValidationNEL[JsFailure, T] =
    fieldWithValidationNel[T](name, ((t: T) => valid(t).toValidationNel), js)

  private[this] def groupFieldJsFailures[T](name: String)(jsV: ValidationNEL[JsFailure, T]): ValidationNEL[JsFailure, T] =
    jsV.fail.map(fs => jsFailureNel(name, fs)).validation

}
