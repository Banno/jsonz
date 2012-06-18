package jsonz
import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import scalaz.{Failure, NonEmptyList, Success, ValidationNEL}

trait JsonzSpec extends Specification with ScalaCheck {
  def beSuccess[A, B](b: B): Matcher[ValidationNEL[A,B]] = { v: ValidationNEL[A,B] =>
    v must beLike {
      case Success(s) => s must beEqualTo(b)
    }
  }

  def containFailure[A, B](a: A): Matcher[ValidationNEL[A,B]] = { v: ValidationNEL[A,B] =>
    v must beLike {
      case Failure(failures) => failures.list must contain(a)
    }
  }

  def containJsFailureStatement[B](statment: String): Matcher[ValidationNEL[JsFailure,B]] = { (v: ValidationNEL[JsFailure, B]) =>
    v must containFailure(JsFailureStatement(statment))
  }

  def containJsFieldFailure[B](path: String, statement: String): Matcher[ValidationNEL[JsFailure,B]] = { (v: ValidationNEL[JsFailure, B]) =>
    v must containFailure(JsFieldFailure(path, NonEmptyList(JsFailureStatement(statement))))
  }



}
