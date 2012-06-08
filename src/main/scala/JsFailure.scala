package jsonz
import scalaz._

sealed trait JsFailure
case class JsFailureStatement(statement: String) extends JsFailure
case class JsFieldFailure(field: String, failures: NonEmptyList[JsFailure]) extends JsFailure

object JsFailure {
  def jsFailureNel(name: String, failures: NonEmptyList[String]): NonEmptyList[JsFailure] =
    NonEmptyList(JsFieldFailure(name, failures.map(JsFailureStatement)))

  def jsFailureValidationNel[A](statement: String): ValidationNEL[JsFailure, A] =
    Failure(JsFailureStatement(statement)).toValidationNel
  def jsFailureValidationNel[A](name: String, statement: String): ValidationNEL[JsFailure, A] =
    Failure(JsFieldFailure(name, NonEmptyList(JsFailureStatement(statement)))).toValidationNel
}
