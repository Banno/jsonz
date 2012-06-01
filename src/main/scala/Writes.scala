package jsonz

trait Writes[T] {
  def writes(o: T): JsValue
}

