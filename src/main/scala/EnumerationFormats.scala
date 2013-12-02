package jsonz
import java.lang.Enum
import scalaz.Validation
import scala.util.control.Exception._

trait EnumerationFormats extends ScalaEnumerationFormats with JavaEnumerationFormats

trait ScalaEnumerationFormats {
  private[this] implicit class EnhancedEnumerationOps[E <: Enumeration](val enum: E) {
    def withNameIgnoreCase(s: String): E#Value = enum.values.find(_.toString.equalsIgnoreCase(s)).get
  }

  def scalaEnumerationFormat[E <: Enumeration](enum: E) = new ScalaEnumerationFormat[E] {
    lazy val enumeration = enum
  }

  protected trait ScalaEnumerationFormat[E <: Enumeration] extends Format[E#Value] {
    def enumeration: E

    def reads(js: JsValue): JsonzValidation[E#Value] = js match {
      case JsString(str) =>
        val maybeSuccessfulEnum = allCatch.opt(enumeration.withNameIgnoreCase(str).asInstanceOf[E#Value])
        maybeSuccessfulEnum.map(Validation.success).getOrElse(JsFailure.jsFailureValidationNel("not a valid value"))

      case _ => JsFailure.jsFailureValidationNel("not a valid value")
    }

    def writes(e: E#Value): JsValue = JsString(e.toString)
  }
}

trait JavaEnumerationFormats {
  def javaEnumerationFormat[E <: Enum[E]](enum: E) = new JavaEnumerationFormat[E] {
    lazy val enumeration = enum
  }

  protected trait JavaEnumerationFormat[E <: Enum[E]] extends Format[E] {
    def enumeration: E

    def reads(js: JsValue): JsonzValidation[E] = js match {
      case JsString(str) =>
        val maybeSuccessfulEnum = allCatch.opt(Enum.valueOf[E](enumeration.getDeclaringClass, str.toUpperCase))
        maybeSuccessfulEnum.map(Validation.success).getOrElse(JsFailure.jsFailureValidationNel("not a valid value"))

      case _ => JsFailure.jsFailureValidationNel("not a valid value")
    }

    def writes(ev: E): JsValue = JsString(ev.name.toUpperCase)
  }
}
