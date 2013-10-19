package jsonz
import jsonz.testkit._
import scalaz.{NonEmptyList, ValidationNel}
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import org.specs2.matcher.{Matcher, MatchersImplicits}

trait JsonzSpec extends Specification with ScalaCheck with MatchersImplicits with Specs2JsonzTestkit {
  def beSuccess[A, B](b: B): Matcher[ValidationNel[A, B]] =
    ((v: ValidationNel[A, B]) => v.map(_ must beEqualTo(b)).toOption.map(_ => success) getOrElse failure("not a success"))

  def containJsFailureStatement[B](statement: String): Matcher[JsonzValidation[B]] =
    ((v: JsonzValidation[B]) => v must containFailure(JsFailureStatement(statement)))

  def containJsFieldFailure[B](path: String, statement: String): Matcher[JsonzValidation[B]] =
    ((v: JsonzValidation[B]) => v must containFailure(JsFieldFailure(path, NonEmptyList(JsFailureStatement(statement)))))
}
