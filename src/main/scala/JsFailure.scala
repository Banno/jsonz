package jsonz
import scalaz._

sealed trait JsFailure
case class JsFailureStatement(statement: String) extends JsFailure
case class JsFieldFailure(field: String, failures: NonEmptyList[JsFailure]) extends JsFailure

object JsFailure {
  import Validation._

  def jsFailure(statement: String): JsFailure = JsFailureStatement(statement)

  def jsFailureNel(name: String, failures: NonEmptyList[JsFailure]): NonEmptyList[JsFailure] =
    NonEmptyList(JsFieldFailure(name, failures))

  def jsFailureValidationNel[A](statement: String): JsonzValidation[A] =
    failure(JsFailureStatement(statement)).toValidationNEL
  def jsFailureValidationNel[A](name: String, statement: String): JsonzValidation[A] =
    failure(JsFieldFailure(name, NonEmptyList(JsFailureStatement(statement)))).toValidationNEL

}
