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

  def scalaEnumerationFormat[E <: Enumeration](enum: E, overrides: (E#Value, String)*) = new ScalaEnumerationFormat[E] {
    lazy val enumeration = enum

    override def writes(e: E#Value): JsValue = JsString(overrides.find(_._1 == e).map(_._2) getOrElse e.toString)

    override def reads(js: JsValue): JsonzValidation[E#Value] = js match {
      case JsString(str) if findStringKeyInOverrides(str).nonEmpty => Validation.success(findStringKeyInOverrides(str).get)
      case _ => super.reads(js)
    }

    private[this] def findStringKeyInOverrides(key: String): Option[E#Value] =
      overrides.find(_._2 equalsIgnoreCase key).map(_._1)
  }

  protected trait ScalaEnumerationFormat[E <: Enumeration] extends Format[E#Value] {
    def enumeration: E

    def reads(js: JsValue): JsonzValidation[E#Value] = js match {
      case JsString(str) => findValueOrFailure(str)
      case jsv => JsFailure.jsFailureValidationNel(s"not one of ${enumeration.values.toList}")
    }

    def writes(e: E#Value): JsValue = JsString(e.toString)

    protected[this] def findValueOrFailure(value: String) =
      findValueInScalaEnum(enumeration, value).map(Validation.success).getOrElse(generateFailureForMissingValue(value, enumeration.values))

    private[this] def generateFailureForMissingValue(key: String, otherValues: E#ValueSet): JsonzValidation[E#Value] =
      JsFailure.jsFailureValidationNel(s"not one of ${otherValues.toList.map(_.toString)}")

    private[this] def findValueInScalaEnum(enum: E, key: String): Option[E#Value] = enum.values.find(_.toString.equalsIgnoreCase(key))
  }
}

trait JavaEnumerationFormats {
  def javaEnumerationFormat[E <: Enum[E]](enum: E) = new JavaEnumerationFormat[E] {
    lazy val enumeration = enum
  }

  protected trait JavaEnumerationFormat[E <: Enum[E]] extends Format[E] {
    def enumeration: E
    private[this] lazy val enumClass = enumeration.getDeclaringClass

    def reads(js: JsValue): JsonzValidation[E] = js match {
      case JsString(str) =>
        val maybeSuccessfulEnum = allCatch.opt(Enum.valueOf[E](enumClass, str.toUpperCase))
        val maybeSuccess = maybeSuccessfulEnum.map(Validation.success)
        maybeSuccess.getOrElse(JsFailure.jsFailureValidationNel(s"not one of ${enumClass.getEnumConstants.toList.map(_.toString)}"))

      case jsv => JsFailure.jsFailureValidationNel(s"not one of ${enumClass.getEnumConstants.toList}")
    }

    def writes(ev: E): JsValue = JsString(ev.name.toUpperCase)
  }
}
