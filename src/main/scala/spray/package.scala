package jsonz
import _root_.spray.http._
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

package object spray {
  trait JsonzMarshalling {
    implicit def writesMarshaller[T : Writes] =
      Marshaller.of[T](ContentTypes.`application/json`) { (value, contentType, ctx) =>
        ctx.marshalTo(HttpEntity(contentType, Jsonz.toJsonBytes(value)))
      }


    implicit def readsUnmarshaller[T : Reads]: Unmarshaller[T] =
      new SimpleUnmarshaller[T] {
        val canUnmarshalFrom: Seq[ContentTypeRange] = Seq(ContentTypeRange(MediaTypes.`application/json`))

        def unmarshal(entity: HttpEntity) = entity match {
          case body: HttpEntity if body.nonEmpty =>
            val v = Jsonz.fromJsonBytes(body.data.toByteArray)
            import DefaultFormats.nonEmptyListFormat
            import DefaultFormats.jsFailureFormat
            v.toEither.left.map(failures => MalformedContent(Jsonz.toJsonStr(failures)))
          case _ => Left(ContentExpected)
        }
      }
  }
  object JsonzMarshalling extends JsonzMarshalling
  implicit def writesMarshaller[T : Writes] = JsonzMarshallers.writesMarshaller
  implicit def readsUnmarshaller[T : Reads] = JsonzMarshallers.readsUnmarshaller
}
