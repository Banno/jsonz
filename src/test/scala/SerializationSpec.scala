package jsonz
import jsonz.models._
import scalaz.Success
import org.scalacheck.{Arbitrary, Prop}

object SerializationSpec extends JsonzSpec {
  import DefaultFormats._

  "Writes arbitrary JsValue's to and from strings" ! check { js: JsValue =>
    val wrote = Jsonz.toJsonStr(js)
    val read = Jsonz.fromJsonStr[JsValue](wrote)
    read must_== Success(js)
  }.pendingUntilFixed("JsNull <-> null")

  "Failure when unparseable" ! pending

}
