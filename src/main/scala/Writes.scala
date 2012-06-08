package jsonz
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Cannot find jsonz.Writes type class for ${T}")
trait Writes[T] {
  def writes(o: T): JsValue
}
