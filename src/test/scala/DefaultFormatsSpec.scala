package jsonz
import jsonz.models._
import scalaz.{Success, Failure, ValidationNEL}
import org.scalacheck.{Arbitrary, Prop}

object DefaultFormatsSpec extends JsonzSpec {
  import DefaultFormats._
  import Jsonz._

  "JsValue" ! check(toAndFrom[JsValue])

  "Int" ! check(toAndFrom[Int])
  "Int failures" ! {
    fromJson[Int](JsString("abc")) must containJsFailureStatement("not an int")
    fromJson[Int](JsNull) must containJsFailureStatement("not an int")
    fromJson[Int](JsBoolean(true)) must containJsFailureStatement("not an int")
    fromJson[Int](JsArray(Nil)) must containJsFailureStatement("not an int")
    fromJson[Int](JsObject(Nil)) must containJsFailureStatement("not an int")
    fromJson[Int](JsNumber(1.23)) must containJsFailureStatement("not an int")
  }.pendingUntilFixed("1.23 is not an int")

  "Short " ! check(toAndFrom[Short])

  "Long" ! check(toAndFrom[Long])

  "Float" ! check(toAndFrom[Float])

  "Double" ! check(toAndFrom[Double])

  "BigDecimal" ! pending // check(toAndFrom[BigDecimal]) // arithmetic overflows

  "Boolean" ! check(toAndFrom[Boolean])
  "Boolean failures" ! {
    fromJson[Boolean](JsString("abc")) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsNull) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsArray(Nil)) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsObject(Nil)) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsNumber(1.23)) must containJsFailureStatement("not a boolean")
   }

  "Map[String, String]" ! check(toAndFrom[Map[String, String]])
  "Map[String, Int]" ! check(toAndFrom[Map[String, Int]])
  "Map[String, Map[String, Int]]" ! check(toAndFrom[Map[String, Map[String, Int]]])
  "Map failures" ! {
    fromJson[Map[String, String]](JsString("abc")) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsNull) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsArray(Nil)) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsBoolean(false)) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsNumber(1.23)) must containJsFailureStatement("not an object")
  }

  "String" ! check(toAndFrom[String])
  "String failures" ! {
    fromJson[String](JsObject(Nil)) must containJsFailureStatement("not a string")
    fromJson[String](JsNull) must containJsFailureStatement("not a string")
    fromJson[String](JsArray(Nil)) must containJsFailureStatement("not a string")
    fromJson[String](JsBoolean(false)) must containJsFailureStatement("not a string")
    fromJson[String](JsNumber(1.23)) must containJsFailureStatement("not a string")
  }

  "List[String]" ! check(toAndFrom[List[String]])
  "List[Int]" ! check(toAndFrom[List[Int]])
  "Seq[String]" ! check(toAndFrom[Seq[String]])
  "List/Seq failures" ! {
    fromJson[List[String]](JsObject(Nil)) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsNull) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsString("abc")) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsBoolean(false)) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsNumber(1.23)) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsArray(Seq(JsBoolean(false)))) must containJsFailureStatement("not a string")
    fromJson[List[String]](JsArray(Seq(JsNumber(1.23), JsBoolean(false)))) must containJsFailureStatement("not a string")
  }

  "Array[String]" ! pending

  "Option[Int]" ! check(toAndFrom[Option[Int]])
  "Option[String]" ! check(toAndFrom[Option[String]])
  "Option[Map[String, List[Int]]]" ! check(toAndFrom[Option[Map[String, List[Int]]]])
  "Option failures" ! {
    fromJson[Option[Int]](JsString("abc")) must containJsFailureStatement("not an int")
  }

  def toAndFrom[T : Format : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = toJson(o)
    val read = fromJson[T](wrote)
    read must_== Success(o)
  }

  def containJsFailureStatement(statment: String) = { (v: ValidationNEL[JsFailure, _]) =>
    v must beLike {
      case Failure(failures) => failures.list must contain(JsFailureStatement(statment))
    }
  }
}
