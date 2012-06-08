package jsonz
import jsonz.models._
import scalaz.Success
import org.scalacheck.{Arbitrary, Prop}

object DefaultFormatsSpec extends JsonzSpec {
  import DefaultFormats._

  "JsValue" ! check(toAndFrom[JsValue])

  "Int" ! check(toAndFrom[Int])

  "Short " ! check(toAndFrom[Short])

  "Long" ! check(toAndFrom[Long])

  "Float" ! check(toAndFrom[Float])

  "Double" ! check(toAndFrom[Double])

  "BigDecimal" ! pending // check(toAndFrom[BigDecimal]) // arithmetic overflows

  "Boolean" ! check(toAndFrom[Boolean])

  "Map" ! pending // check(toAndFrom[Map[String, String]])

  "String" ! check(toAndFrom[String])

  "Seq/List/Traversable" ! pending

  "Option[Int]" ! pending
  "Option[String]" ! pending
  "Option[Map[String, List[String]]]" ! pending

  "Pair[String, Boolean]" ! pending

  // Maybe JsArray of size 2 to Pair (icky)
  "Product" ! pending

  def toAndFrom[T : Format : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = Jsonz.toJson(o)
    val read = Jsonz.fromJson[T](wrote)
    read must_== Success(o)
  }
}
