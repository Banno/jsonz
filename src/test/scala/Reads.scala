package jsonz
import scalaz._

case class JsFieldFailure(field: String, failures: NonEmptyList[String]) {
  def toValidationNel[T]: ValidationNEL[JsFieldFailure, T] =
    Failure(this).toValidationNel
}

trait Reads[T] {
  def reads(js: JsValue): ValidationNEL[JsFieldFailure, T]
}

