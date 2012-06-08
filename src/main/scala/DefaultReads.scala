package jsonz
import scalaz._
import scalaz.syntax._

object DefaultReads extends DefaultReads

trait DefaultReads {
  implicit object StringReads extends Reads[String] {
    def reads(js: JsValue) = js match {
      case JsString(str) => Success(str).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a string")).toValidationNel
    }
  }

  implicit object IntReads extends Reads[Int] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toInt).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not an int")).toValidationNel
    }
  }

  implicit object ShortReads extends Reads[Short] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toShort).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a short")).toValidationNel
    }
  }

  implicit object LongReads extends Reads[Long] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toLong).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a long")).toValidationNel
    }
  }

  implicit object FloatReads extends Reads[Float] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toFloat).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a float")).toValidationNel
    }
  }

  implicit object DoubleReads extends Reads[Double] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toDouble).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a short")).toValidationNel
    }
  }

  implicit object BigDecimalReads extends Reads[BigDecimal] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a double")).toValidationNel
    }
  }

  implicit object BooleanReads extends Reads[Boolean] {
    def reads(js: JsValue) = js match {
      case JsBoolean(bool) => Success(bool).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a boolean")).toValidationNel
    }
  }

  implicit object JsValueReads extends Reads[JsValue] {
    def reads(js: JsValue) = Success(js).toValidationNel
  }
}
