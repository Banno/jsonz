package jsonz.spray
import jsonz._
import jsonz.models._
import _root_.spray.http._
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

object SpraySpec extends JsonzSpec {
  import DefaultFormats._

  "provides a marshaller for anything that has a Writes[T]" in check { jsv: JsValue =>
    marshal(jsv) must beRight(
      HttpBody(ContentType.`application/json`, Jsonz.toJsonBytes(jsv))
    )
  }

  "provides an unmarshaller for anything that has a Reads[T]" in check { jsv: JsValue =>
    val body = HttpBody(ContentType.`application/json`, Jsonz.toJsonBytes(jsv))
    body.as[JsValue] must beRight(jsv)
  }
}
