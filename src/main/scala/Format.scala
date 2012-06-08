package jsonz
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Cannot find jsonz.Format type class for ${T}")
trait Format[T] extends Writes[T] with Reads[T]
