package jsonz.spray
import jsonz._
import jsonz.models._
import _root_.spray.http._
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

object SpraySpec extends JsonzSpec {
  import DefaultFormats._

  "marshaller" should {
    "provide for anything that has a Writes[T]" in prop { jsv: JsValue =>
      marshal(jsv) must beRight(
        HttpEntity(ContentTypes.`application/json`, Jsonz.toJsonBytes(jsv))
      )
    }
  }

  "unmarshaller" should {
    "provide for anything that has a Reads[T]" in prop { jsv: JsValue =>
      val body = HttpEntity(ContentTypes.`application/json`, Jsonz.toJsonBytes(jsv))
      body.as[JsValue] must beRight(jsv)
    }

    "become a DeserializationError if bad json is read in" in {
      val body = HttpEntity(ContentTypes.`application/json`, "bad json")
      body.as[JsValue] must beLeft(MalformedContent("[\"not valid JSON\"]"))
    }
  }
}
