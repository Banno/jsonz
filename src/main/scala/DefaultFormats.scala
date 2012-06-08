package jsonz

object DefaultFormats extends DefaultReads with DefaultWrites {
  implicit def genericFormat[T](implicit jsr: Reads[T], jsw: Writes[T]) = new Format[T] {
    def reads(js: JsValue) = jsr.reads(js)
    def writes(o: T) = jsw.writes(o)
  }
}
