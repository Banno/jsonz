package jsonz
import com.codahale.jerkson.ParsingException
import scalaz.{Failure, Success, Validation, ValidationNEL}

object Jsonz {
  import JsFailure._
  import Validation._

  def toJsonStr[T: Writes](o: T) = stringify(toJson(o))
  def fromJsonStr[T: Reads](str: String) = parse(str).flatMap(fromJson[T])

  def toJsonBytes[T: Writes](o: T) = stringify(toJson(o)).getBytes("UTF-8")
  def fromJsonBytes[T: Reads](bytes: Array[Byte]) = parse(bytes).flatMap(fromJson[T])

  import java.io.{InputStream, OutputStream}
  def toJsonOutputStream[T: Writes](o: T, out: OutputStream) =
    JerksonJson.generate(toJson(o), out)
  def fromJsonInputStream[T: Reads](in: InputStream) = parse(in).flatMap(fromJson[T])

  def toJson[T](o: T)(implicit jsw: Writes[T]) = jsw.writes(o)
  def fromJson[T](js: JsValue)(implicit jsr: Reads[T]): JsonzValidation[T] =
    jsr.reads(js)

  def parse(s: String) = tryToParse(JerksonJson.parse[JsValue](s))
  def parse(bytes: Array[Byte]) = tryToParse(JerksonJson.parse[JsValue](bytes))
  def parse(in: InputStream) = tryToParse(JerksonJson.parse[JsValue](in))
  def stringify(js: JsValue): String = JerksonJson.generate(js)

  private[this] def tryToParse(f: => JsValue): JsonzValidation[JsValue] = try {
    success(f).toValidationNEL
  } catch {
    case _: ParsingException => jsFailureValidationNel("not valid JSON")
  }
}
