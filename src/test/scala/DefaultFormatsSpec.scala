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

  "Map[String, String]" ! check(toAndFrom[Map[String, String]])
  "Map[String, Int]" ! check(toAndFrom[Map[String, Int]])
  "Map[String, Map[String, Int]]" ! check(toAndFrom[Map[String, Map[String, Int]]])

  "String" ! check(toAndFrom[String])

  "List[String]" ! check(toAndFrom[List[String]])
  "List[Int]" ! check(toAndFrom[List[Int]])
  "Seq[String]" ! check(toAndFrom[Seq[String]])

  "Array[String]" ! pending

  "Option[Int]" ! check(toAndFrom[Option[Int]])
  "Option[String]" ! check(toAndFrom[Option[String]])
  "Option[Map[String, List[String]]]" ! pending

  "Failure scenarios" ! pending

  def toAndFrom[T : Format : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = Jsonz.toJson(o)
    val read = Jsonz.fromJson[T](wrote)
    read must_== Success(o)
  }
}
