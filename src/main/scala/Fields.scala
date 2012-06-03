package jsonz
import scalaz._

object Fields extends Fields

trait Fields {
  def field[T: Reads](name: String, js: JsValue): ValidationNEL[JsFieldFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      maybeFromJson getOrElse fieldFailure(name, "is missing").toValidationNel
    }
    case _ => fieldFailure("", "is not an object").toValidationNel
  }

  def fieldWithValidation[T: Reads](name: String, valid: (T) => ValidationNEL[String, T], js: JsValue): ValidationNEL[JsFieldFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      val fromjson = maybeFromJson getOrElse fieldFailure(name, "is missing").toValidationNel
      fromjson.flatMap { value =>
        valid(value).fold(failure = (fs => Failure(NonEmptyList(JsFieldFailure(name, fs)))),
                          success = (x => Success(x)))
      }
    }
    case _ => fieldFailure("", "is not an object").toValidationNel
  }

  def fieldFailure(name: String, failure: String) = JsFieldFailure(name, NonEmptyList(failure))

}
