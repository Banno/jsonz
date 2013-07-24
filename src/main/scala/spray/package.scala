package jsonz
import _root_.spray.http._
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

package object spray {
  implicit def writesMarshaller[T : Writes] =
    Marshaller.of[T](ContentTypes.`application/json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpEntity(contentType, Jsonz.toJsonBytes(value)))
    }


  implicit def readsUnmarshaller[T : Reads]: Unmarshaller[T] =
    new SimpleUnmarshaller[T] {
      val canUnmarshalFrom: Seq[ContentTypeRange] = Seq(MediaTypes.`application/json`)

      def unmarshal(entity: HttpEntity) = entity match {
        case body: HttpBody =>
          val v = Jsonz.fromJsonBytes(body.buffer)
          import DefaultFormats.nonEmptyListFormat
          import DefaultFormats.jsFailureFormat
          v.toEither.left.map(failures => MalformedContent(Jsonz.toJsonStr(failures)))
        case EmptyEntity => Left(ContentExpected)
      }
    }
}
