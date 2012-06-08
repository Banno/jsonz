package jsonz
import scalaz._
import scala.annotation.implicitNotFound

case class JsFieldFailure(field: String, failures: NonEmptyList[String]) {
  def toValidationNel[T]: ValidationNEL[JsFieldFailure, T] =
    Failure(this).toValidationNel
}

@implicitNotFound(msg = "Cannot find jsonz.Reads type class for ${T}")
trait Reads[T] {
  def reads(js: JsValue): ValidationNEL[JsFieldFailure, T]
}
