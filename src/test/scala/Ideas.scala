package jsonz
import jsonz.models._
import scalaz._

object Ideas extends JsonzSpec {
  "reader monads" in {
    import Scalaz._

    val jso = JsObject("k" -> JsString("v") :: Nil)

    // for {
    //   f <- field_c[String]("k")
    // } yield (f |@| f)

    success
  }
}

