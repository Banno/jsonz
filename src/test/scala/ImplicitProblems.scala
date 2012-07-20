package eg.testing
import jsonz._

case class SomethingElse(i: Int, j: String)
case class ContainingSomethingElse(s: SomethingElse)

object BasicJsonFormats {
  import DefaultFormats._

  implicit object SomethingElseWrites extends Writes[SomethingElse] {
    def writes(s: SomethingElse) =
      JsObject("i" -> Jsonz.toJson(s.i) ::
               "j" -> Jsonz.toJson(s.j) ::
               Nil)
  }
}

object UsingThem {
  import DefaultFormats._
  import BasicJsonFormats._

  implicit object ContainingSomethingElse extends Writes[ContainingSomethingElse] {
    def writes(c: ContainingSomethingElse) =
      JsObject("c" -> Jsonz.toJson(c.s) :: Nil)
  }
}
