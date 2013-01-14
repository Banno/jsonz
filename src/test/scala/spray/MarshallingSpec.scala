package jsonz.spray
import jsonz._
import jsonz.models._
import _root_.spray.http._
import _root_.spray.httpx.marshalling._

object MarshallingSpec extends JsonzSpec {
  "provides a marshaller for anything that has a Writes[T]" in check { person: Person =>
    marshal[Person](person) must beRight(
      HttpBody(ContentType.`application/json`, Jsonz.toJsonBytes[Person](person))
    )
  }
}
