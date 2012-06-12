package jsonz
import scalaz.Success

object ProductFormatsSpecs extends JsonzSpec {
  import DefaultFormats._
  import ProductFormats._
  import Jsonz._

  case class Testing2(one: String, two: Option[Int])
  implicit val testing2Format: Format[Testing2] = productFormat2("one", "two")(Testing2)(Testing2.unapply)

  case class Testing3(one: String, two: Option[Testing2], three: Int)
  implicit val testing3Format: Format[Testing3] = productFormat3("one", "two", "three")(Testing3)(Testing3.unapply)

  "easy product formats" ! {
    val test = Testing3("abc", Some(Testing2("def", None)), 123)
    val result = fromJson[Testing3](toJson(test))
    result must_== Success(test)
  }
}
