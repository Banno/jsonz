package jsonz

object GenericFormat extends GenericFormat

trait GenericFormat {
  implicit def genericFormat[T](implicit jsr: Reads[T], jsw: Writes[T]) = new Format[T] {
    def reads(js: JsValue) = jsr.reads(js)
    def writes(o: T) = jsw.writes(o)
  }
}
