package jsonz
import _root_.spray.http._
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

package object spray {
  implicit def writesMarshaller[T : Writes] =
    Marshaller.of[T](ContentType.`application/json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpBody(contentType, Jsonz.toJsonBytes(value)))
    }

  implicit def readsMarshaller[T : Reads]: Unmarshaller[T] =
    Unmarshaller[T](MediaTypes.`application/json`) {
      case body: HttpBody =>
        Jsonz.fromJsonStr(body.asString).toEither.right.get
    }
}
