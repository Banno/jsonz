package jsonz
import scalaz._
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Cannot find jsonz.Reads type class for ${T}")
trait Reads[T] {
  def reads(js: JsValue): JsonzValidation[T]
}
