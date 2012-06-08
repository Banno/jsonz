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

  "Map" ! pending

  "String" ! check(toAndFrom[String])

  "Seq/List/Traversable" ! pending

  "Option" ! pending

  "Product" ! pending

  "Failure scenarios" ! pending

  def toAndFrom[T : Format : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = Jsonz.toJson(o)
    val read = Jsonz.fromJson[T](wrote)
    read must_== Success(o)
  }
}
