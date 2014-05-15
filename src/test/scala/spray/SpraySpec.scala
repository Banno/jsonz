package jsonz.spray
import jsonz._
import jsonz.models._
import scalaz.effect.IO
import _root_.spray.http._
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

object SpraySpec extends JsonzSpec with ExtendedSpraySupport {
  import scalaz._
  import DefaultFormats._

  "marshaller" should {
    "provide for anything that has a Writes[T]" in check { jsv: JsValue =>
      marshal(jsv) must beRight(
        HttpEntity(ContentTypes.`application/json`, Jsonz.toJsonBytes(jsv))
      )
    }

    "provide for disjunctions" in {
      val left: Throwable \/ Int = -\/(new Throwable{})
      val right: Throwable \/ Int = \/-(10)

      marshal(left) must beLeft
      marshal(right) must beRight(
        HttpEntity(ContentTypes.`application/json`, Jsonz.toJsonBytes(10))
      )
    }

    "provide for IO Monads" in {
      val succ = IO("hello world")
      val fail = IO(new Throwable{})

      marshal(fail) must beLeft
      marshal(succ) must beRight(
        HttpEntity(ContentTypes.`application/json`, Jsonz.toJsonBytes("hello world"))
      )
    }
  }

  "unmarshaller" should {
    "provide for anything that has a Reads[T]" in check { jsv: JsValue =>
      val body = HttpEntity(ContentTypes.`application/json`, Jsonz.toJsonBytes(jsv))
      body.as[JsValue] must beRight(jsv)
    }

    "become a DeserializationError if bad json is read in" in {
      val body = HttpEntity(ContentTypes.`application/json`, "bad json")
      body.as[JsValue] must beLeft(MalformedContent("[\"not valid JSON\"]"))
    }
  }
}
