package jsonz

object DefaultWrites extends DefaultWrites

trait DefaultWrites {
  implicit object IntWrites extends Writes[Int] {
    def writes(o: Int) = JsNumber(o)
  }

  implicit object StringWrites extends Writes[String] {
    def writes(o: String) = JsString(o)
  }

  implicit object JsValueWrites extends Writes[JsValue] {
    def writes(js: JsValue) = js
  }
}
