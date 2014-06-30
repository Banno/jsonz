package jsonz
import org.specs2.matcher._
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import scalaz.{NonEmptyList, ValidationNel}

trait JsonzSpec extends Specification with ScalaCheck with MatchersImplicits {
  def beSuccess[A, B]: Matcher[ValidationNel[A, B]] = { v: ValidationNel[A, B] =>
    v.isSuccess
  }

  def beSuccess[A, B](b: B): Matcher[ValidationNel[A,B]] = { v: ValidationNel[A,B] =>
    v.map(_ == b) getOrElse false
  }

  def containFailure[A, B](a: A): Matcher[ValidationNel[A,B]] = { v: ValidationNel[A,B] =>
    v.swap.map(_.list.contains(a)) getOrElse false
  }

  def haveFailureCount[A, B](n: Int): Matcher[ValidationNel[A,B]] = { v: ValidationNel[A,B] =>
    v.swap.map(_.list.size == n) getOrElse false
  }

  def containJsFailureStatement[B](statment: String): Matcher[JsonzValidation[B]] = { (v: JsonzValidation[B]) =>
    v must containFailure(JsFailureStatement(statment))
  }

  def containJsFieldFailure[B](path: String, statement: String): Matcher[JsonzValidation[B]] = { (v: JsonzValidation[B]) =>
    v must containFailure(JsFieldFailure(path, NonEmptyList(JsFailureStatement(statement))))
  }
}
