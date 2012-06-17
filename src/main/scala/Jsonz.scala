package jsonz
import com.codahale.jerkson.ParsingException
import scalaz.{Failure, Success, Validation, ValidationNEL}

object Jsonz {
  import JsFailure._

  def parse(s: String): Validation[JsFailure, JsValue] = try {
    Success(JerksonJson.parse[JsValue](s))
  } catch {
    case _: ParsingException => Failure(jsFailure("not valid JSON"))
  }

  def fromJson[T](js: JsValue)(implicit jsr: Reads[T]): ValidationNEL[JsFailure, T] =
    jsr.reads(js)

  def stringify(js: JsValue): String = JerksonJson.generate(js)
  def toJson[T](o: T)(implicit jsw: Writes[T]) = jsw.writes(o)

  def toJsonStr[T: Writes](o: T) = stringify(toJson(o))
  def fromJsonStr[T: Reads](str: String) = parse(str).toValidationNel.flatMap(fromJson[T])
}
