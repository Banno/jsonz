package jsonz
import scalaz.Success

object ProductFormatsSpecs extends JsonzSpec {
  import Jsonz._
  import DefaultFormats._

  case class Testing2(one: String, two: Option[Int])
  implicit val testing2Format: Format[Testing2] = productFormat2("one", "two")(Testing2)(Testing2.unapply)

  case class Testing3(one: String, two: Option[Testing2], three: Int)
  implicit val testing3Format: Format[Testing3] = productFormat3("one", "two", "three")(Testing3)(Testing3.unapply)

  "easy product formats" ! {
    val test = Testing3("abc", Some(Testing2("def", None)), 123)
    val result = fromJson[Testing3](toJson(test))
    result must_== Success(test)
  }

  "allow for optional fields to not have to be present in the json" ! {
    // do this via manifest or productFormat2("one", optional("two")) pimping
    val result = fromJson[Testing2](JsObject(Seq("one" -> JsString("hello"))))
    result must_== Success(Testing2("one", None))
  }.pendingUntilFixed("need to check for optional fields")
}
