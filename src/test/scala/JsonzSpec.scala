package jsonz
import jsonz.specs2._
import scalaz.{NonEmptyList, ValidationNel}
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import org.specs2.scalaz.ValidationMatchers
import org.specs2.matcher.{Matcher, MatchersImplicits}
import org.scalacheck.Properties

trait JsonzSpec extends Specification with ScalaCheck with MatchersImplicits with Specs2JsonzTestkit with ValidationMatchers {

  def checkAll(name: String, props: Properties) =
    props.properties.foreach {
      case (name2, prop) =>
        name in {
          name2 ! prop
        }
    }

  def containJsFailureStatement[B](statement: String): Matcher[JsonzValidation[B]] =
    ((v: JsonzValidation[B]) => v must containFailure(JsFailureStatement(statement)))

  def containJsFieldFailure[B](path: String, statement: String): Matcher[JsonzValidation[B]] =
    ((v: JsonzValidation[B]) => v must containFailure(JsFieldFailure(path, NonEmptyList(JsFailureStatement(statement)))))
}
