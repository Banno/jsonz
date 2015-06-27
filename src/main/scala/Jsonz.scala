package jsonz
import scalaz.{Failure, Success, Validation, ValidationNel}
import scalaz.Validation.FlatMap._
import java.io.IOException

object Jsonz {
  import JsFailure._

  def toJsonStr[T: Writes](o: T) = stringify(toJson(o))
  def fromJsonStr[T: Reads](str: String) = parse(str).flatMap(fromJson[T])

  def toJsonBytes[T: Writes](o: T) = stringify(toJson(o)).getBytes("UTF-8")
  def fromJsonBytes[T: Reads](bytes: Array[Byte]) = parse(bytes).flatMap(fromJson[T])

  import java.io.{InputStream, OutputStream}
  def toJsonOutputStream[T: Writes](o: T, out: OutputStream) =
    JacksonJson.generate(toJson(o), out)
  def fromJsonInputStream[T: Reads](in: InputStream) = parse(in).flatMap(fromJson[T])

  def toJson[T](o: T)(implicit jsw: Writes[T]) = jsw.writes(o)
  def fromJson[T](js: JsValue)(implicit jsr: Reads[T]): JsonzValidation[T] =
    jsr.reads(js)

  def parse(s: String) = tryToParse(JacksonJson.parse[JsValue](s))
  def parse(bytes: Array[Byte]) = tryToParse(JacksonJson.parse[JsValue](bytes))
  def parse(in: InputStream) = tryToParse(JacksonJson.parse[JsValue](in))
  def stringify(js: JsValue): String = JacksonJson.generate(js)

  private[this] def tryToParse(f: => JsValue): JsonzValidation[JsValue] = try {
    Validation.success(f).toValidationNel
  } catch {
    case _: ParseException   => jsFailureValidationNel("issue reading json")
    case _: ParsingException => jsFailureValidationNel("not valid JSON")
    case e: IOException      => jsFailureValidationNel("problem reading json")
  }
}
