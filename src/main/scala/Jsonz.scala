package jsonz
import com.codahale.jerkson.ParsingException
import scalaz.{Failure, Success, Validation, ValidationNEL}

object Jsonz {
  import JsFailure._

  def toJsonStr[T: Writes](o: T) = stringify(toJson(o))
  def fromJsonStr[T: Reads](str: String) = parse(str).flatMap(fromJson[T])

  def toJsonBytes[T: Writes](o: T) = stringify(toJson(o)).getBytes("UTF-8")
  def fromJsonBytes[T: Reads](bytes: Array[Byte]) = parse(bytes).flatMap(fromJson[T])

  def toJson[T](o: T)(implicit jsw: Writes[T]) = jsw.writes(o)
  def fromJson[T](js: JsValue)(implicit jsr: Reads[T]): ValidationNEL[JsFailure, T] =
    jsr.reads(js)

  def parse(s: String) = tryToParse(JerksonJson.parse[JsValue](s))
  def parse(bytes: Array[Byte]) = tryToParse(JerksonJson.parse[JsValue](bytes))
  def stringify(js: JsValue): String = JerksonJson.generate(js)

  private[this] def tryToParse(f: => JsValue): ValidationNEL[JsFailure, JsValue] = try {
    Success(f).toValidationNel
  } catch {
    case _: ParsingException =>
      Failure(jsFailure("not valid JSON")).toValidationNel
  }
}
