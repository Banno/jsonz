package jsonz

object LazyFormatSpec extends JsonzSpec {
  import DefaultFormats._
  import Jsonz._

  case class RecursiveType(rs: List[RecursiveType])

  implicit lazy val RecursiveTypeFormat: Format[RecursiveType] =
    lazyFormat(productFormat1("rs")(RecursiveType.apply)(RecursiveType.unapply))

  "use lazy format for recursive types" in {
    val recursive = RecursiveType(List(RecursiveType(Nil)))
    val js = toJson(recursive)
    fromJson[RecursiveType](js) must beSuccessful(recursive)
  }
}
