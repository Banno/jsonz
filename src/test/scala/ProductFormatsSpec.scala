package jsonz
import scalaz.Success

object ProductFormatsSpecs extends JsonzSpec {
  import Jsonz._
  import DefaultFormats._

  case class Testing1(one: String)
  implicit val testing1Format: Format[Testing1] = productFormat1("one")(Testing1)(Testing1.unapply)

  case class Testing2(one: String, two: Option[Int])
  implicit val testing2Format: Format[Testing2] = productFormat2("one", "two")(Testing2)(Testing2.unapply)

  case class Testing3(one: String, two: Option[Testing2], three: Int)
  implicit val testing3Format: Format[Testing3] = productFormat3("one", "two", "three")(Testing3)(Testing3.unapply)

  case class Testing4(one: String, two: Option[Option[Int]])
  implicit val testing4Format: Format[Testing4] = productFormat2("one", "two")(Testing4)(Testing4.unapply)

  "easy product formats" ! {
    val test = Testing3("abc", Some(Testing2("def", None)), 123)
    val result = fromJson[Testing3](toJson(test))
    result must_== Success(test)
  }

  "allow for optional fields to not have to be present in the json" ! {
    val result = fromJson[Testing2](JsObject(Seq("one" -> JsString("hello"))))
    result must_== Success(Testing2("hello", None))
  }

  "allow for optional optional fields that are null to be Some(None)" ! {
    fromJson[Testing4](JsObject(Seq("one" -> JsString("hello"), "two" -> JsNull))) must_== Success(Testing4("hello", Some(None)))
    fromJson[Testing4](JsObject(Seq("one" -> JsString("hello"), "two" -> JsNumber(2)))) must_== Success(Testing4("hello", Some(Some(2))))
    fromJson[Testing4](JsObject(Seq("one" -> JsString("hello")))) must_== Success(Testing4("hello", None))
  }

  "allow for product-1's" ! {
    val result = fromJson[Testing1](JsObject(Seq("one" -> JsString("hello"))))
    result must_== Success(Testing1("hello"))
  }
}
