package jsonz
import _root_.spray.http._
import _root_.spray.httpx.marshalling._

package object spray {
  implicit def writesMarshaller[T: Writes] =
    Marshaller.of[T](ContentType.`application/json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpBody(contentType, Jsonz.toJsonBytes(value)))
    }
}
