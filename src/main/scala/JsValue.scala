package jsonz

sealed trait JsValue
case object JsNull extends JsValue
case class JsBoolean(value: Boolean) extends JsValue
case class JsString(value: String) extends JsValue
case class JsNumber(value: BigDecimal) extends JsValue
case class JsArray(elements: Seq[JsValue]) extends JsValue // why seq and not iterable
case class JsObject(fields: Seq[(String, JsValue)]) extends JsValue {
  def get(key: String): Option[JsValue] =
    fields.find(p => p._1 == key).map(p => p._2)
}
