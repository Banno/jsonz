package jsonz
import scalaz.Success

object ProductFormatsSpecs extends JsonzSpec {
  import DefaultFormats._
  import ProductFormats._
  import Jsonz._

  case class Testing2(one: String, two: Option[Int])
  implicit val testingFormat: Format[Testing2] = productFormat2("one", "two")(Testing2)(Testing2.unapply)

  "easy product formats" ! {
    val test = Testing2("abc", None)
    val result = fromJson[Testing2](toJson(test))
    result must_== Success(test)
  }
}
