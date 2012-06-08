package jsonz
import scalaz._
import scalaz.syntax._

object DefaultReads extends DefaultReads

trait DefaultReads {
  import JsFailure._

  implicit object StringReads extends Reads[String] {
    def reads(js: JsValue) = js match {
      case JsString(str) => Success(str).toValidationNel
      case _ => jsFailureValidationNel("not a string")
    }
  }

  implicit object IntReads extends Reads[Int] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toInt).toValidationNel
      case _ => jsFailureValidationNel("not an int")
    }
  }

  implicit object ShortReads extends Reads[Short] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toShort).toValidationNel
      case _ => jsFailureValidationNel("not a short")
    }
  }

  implicit object LongReads extends Reads[Long] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toLong).toValidationNel
      case _ => jsFailureValidationNel("not a long")
    }
  }

  implicit object FloatReads extends Reads[Float] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toFloat).toValidationNel
      case _ => jsFailureValidationNel("not a float")
    }
  }

  implicit object DoubleReads extends Reads[Double] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toDouble).toValidationNel
      case _ => jsFailureValidationNel("not a double")
    }
  }

  implicit object BigDecimalReads extends Reads[BigDecimal] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num).toValidationNel
      case _ => jsFailureValidationNel("not a arbitrary decimal")
    }
  }

  implicit object BooleanReads extends Reads[Boolean] {
    def reads(js: JsValue) = js match {
      case JsBoolean(bool) => Success(bool).toValidationNel
      case _ => jsFailureValidationNel("not a boolean")
    }
  }

  implicit object JsValueReads extends Reads[JsValue] {
    def reads(js: JsValue) = Success(js).toValidationNel
  }
}
