package jsonz
import java.lang.Enum
import scalaz.Validation
import scala.util.control.Exception._
import scala.collection.JavaConversions

trait EnumerationFormats extends ScalaEnumerationFormats with JavaEnumerationFormats

trait ScalaEnumerationFormats {
  def scalaEnumerationFormat[E <: Enumeration](enum: E) = new ScalaEnumerationFormat[E] {
    lazy val enumeration = enum
  }

  protected trait ScalaEnumerationFormat[E <: Enumeration] extends Format[E#Value] {
    def enumeration: E

    def reads(js: JsValue): JsonzValidation[E#Value] = js match {
      case JsString(str) => findValueInScalaEnum(enumeration, str).map(Validation.success).getOrElse(generateFailureForMissingValue(str, enumeration.values))
      case jsv => JsFailure.jsFailureValidationNel(s"JsValue ${jsv} was not a JsString")
    }

    def writes(e: E#Value): JsValue = JsString(e.toString)

    private[this] def generateFailureForMissingValue(key: String, otherValues: E#ValueSet): JsonzValidation[E#Value] =
      JsFailure.jsFailureValidationNel(s"The value ${key} was not found among ${otherValues.toList.map(_.toString)}")

    private[this] def findValueInScalaEnum(enum: E, key: String): Option[E#Value] = enum.values.find(_.toString.equalsIgnoreCase(key))
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
        val enumClass = enumeration.getDeclaringClass
      val maybeSuccessfulEnum = allCatch.opt(Enum.valueOf[E](enumClass, str.toUpperCase))
      val maybeSuccess = maybeSuccessfulEnum.map(Validation.success)
      maybeSuccess.getOrElse(JsFailure.jsFailureValidationNel(s"The value ${str} was not found among ${enumClass.getEnumConstants.toList.map(_.toString)}"))

      case jsv => JsFailure.jsFailureValidationNel(s"JsValue ${jsv} was not a JsString")
    }

    def writes(ev: E): JsValue = JsString(ev.name.toUpperCase)
  }
}
