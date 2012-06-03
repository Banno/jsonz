package jsonz
import scalaz._
import scalaz.syntax._

object DefaultReads {
  implicit object StringReads extends Reads[String] {
    def reads(js: JsValue) = js match {
      case JsString(str) => Success(str).toValidationNel
      case _ => Failure(Fields.fieldFailure("", "not a string")).toValidationNel
    }
  }

  implicit object IntReads extends Reads[Int] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) =>
        Success(num.toInt).toValidationNel // check non int numbers
      case _ => Failure(Fields.fieldFailure("", "not a string")).toValidationNel
    }
  }

  implicit object JsValueReads extends Reads[JsValue] {
    def reads(js: JsValue) = Success(js).toValidationNel
  }
}
