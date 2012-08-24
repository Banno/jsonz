package jsonz
import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import scalaz.{NonEmptyList, ValidationNEL}

trait JsonzSpec extends Specification with ScalaCheck {
  def beSuccess[A, B](b: B): Matcher[ValidationNEL[A,B]] = { v: ValidationNEL[A,B] =>
    v.map(_ must beEqualTo(b)).getOrElse(failure("not a success"))
  }

  def containFailure[A, B](a: A): Matcher[ValidationNEL[A,B]] = { v: ValidationNEL[A,B] =>
    v.swap.map(_.list must contain(a)).getOrElse(failure("not a failure"))
  }

  def containJsFailureStatement[B](statment: String): Matcher[JsonzValidation[B]] = { (v: JsonzValidation[B]) =>
    v must containFailure(JsFailureStatement(statment))
  }

  def containJsFieldFailure[B](path: String, statement: String): Matcher[JsonzValidation[B]] = { (v: JsonzValidation[B]) =>
    v must containFailure(JsFieldFailure(path, NonEmptyList(JsFailureStatement(statement))))
  }

}
