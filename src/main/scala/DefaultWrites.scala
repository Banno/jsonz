package jsonz

object DefaultWrites extends DefaultWrites

trait DefaultWrites {
  implicit object IntWrites extends Writes[Int] {
    def writes(o: Int) = JsNumber(o)
  }

  implicit object ShortWrites extends Writes[Short] {
    def writes(o: Short) = JsNumber(o)
  }

  implicit object LongWrites extends Writes[Long] {
    def writes(o: Long) = JsNumber(o)
  }

  implicit object FloatWrites extends Writes[Float] {
    def writes(o: Float) = JsNumber(o)
  }

  implicit object DoubleWrites extends Writes[Double] {
    def writes(o: Double) = JsNumber(o)
  }

  implicit object BigDecimalWrites extends Writes[BigDecimal] {
    def writes(o: BigDecimal) = JsNumber(o)
  }

  implicit object BooleanWrites extends Writes[Boolean] {
    def writes(o: Boolean) = JsBoolean(o)
  }

  implicit object StringWrites extends Writes[String] {
    def writes(o: String) = JsString(o)
  }

  implicit object JsValueWrites extends Writes[JsValue] {
    def writes(js: JsValue) = js
  }

  implicit def mapWrites[V](implicit vw: Writes[V]) = new Writes[Map[String, V]] {
    def writes(m: Map[String, V]) = JsObject(m.mapValues(v => vw.writes(v)).toSeq)
  }

  implicit def optionWrites[T](implicit tw: Writes[T]) = new Writes[Option[T]] {
    def writes(o: Option[T]) = o.map(tw.writes).getOrElse(JsNull)
  }
}
