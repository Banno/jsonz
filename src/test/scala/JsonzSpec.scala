package jsonz
import org.specs2.matcher.{Matcher, MustExpectable}
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import scalaz.{NonEmptyList, ValidationNel}

trait JsonzSpec extends Specification with ScalaCheck {
  def beSuccess[A, B](b: B): Matcher[ValidationNel[A,B]] = { v: ValidationNel[A,B] =>
    v.map(_ must beEqualTo(b)).getOrElse(Matcher.failure("not a success", MustExpectable(b)))
  }

  def containFailure[A, B](a: A): Matcher[ValidationNel[A,B]] = { v: ValidationNel[A,B] =>
    v.swap.map(_.list must contain(a)).getOrElse(Matcher.failure("not a failure", MustExpectable(a)))
  }

  def haveFailureCount[A, B](n: Int): Matcher[ValidationNel[A,B]] = { v: ValidationNel[A,B] =>
    v.swap.map(_.list must haveSize(n)).getOrElse(Matcher.failure("not a failure", MustExpectable(n)))
  }

  def containJsFailureStatement[B](statment: String): Matcher[JsonzValidation[B]] = { (v: JsonzValidation[B]) =>
    v must containFailure(JsFailureStatement(statment))
  }

  def containJsFieldFailure[B](path: String, statement: String): Matcher[JsonzValidation[B]] = { (v: JsonzValidation[B]) =>
    v must containFailure(JsFieldFailure(path, NonEmptyList(JsFailureStatement(statement))))
  }
}
