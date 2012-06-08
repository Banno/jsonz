package jsonz
import scalaz.ValidationNEL

object Jsonz {
  def parse(s: String): JsValue = JerksonJson.parse[JsValue](s)
  def fromJson[T](js: JsValue)(implicit jsr: Reads[T]): ValidationNEL[JsFailure, T] =
    jsr.reads(js)

  def stringify(js: JsValue): String = JerksonJson.generate(js)
  def toJson[T](o: T)(implicit jsw: Writes[T]) = jsw.writes(o)

  def toJsonStr[T: Writes](o: T) = stringify(toJson(o))
  def fromJsonStr[T: Reads](str: String) = fromJson(parse(str))
  // toJsonBytes
  // fromJsonBytes
  // streaming
}
