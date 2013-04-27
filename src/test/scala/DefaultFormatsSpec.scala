package jsonz
import jsonz.models._
import scalaz.{NonEmptyList, Success, Failure, ValidationNel}
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
  }

  "Short" ! check(toAndFrom[Short])
  "Short failures" ! {
    fromJson[Short](JsString("abc")) must containJsFailureStatement("not a short")
    fromJson[Short](JsNull) must containJsFailureStatement("not a short")
    fromJson[Short](JsBoolean(true)) must containJsFailureStatement("not a short")
    fromJson[Short](JsArray(Nil)) must containJsFailureStatement("not a short")
    fromJson[Short](JsObject(Nil)) must containJsFailureStatement("not a short")
    fromJson[Short](JsNumber(1.23)) must containJsFailureStatement("not a short")
    fromJson[Short](JsNumber(32768)) must containJsFailureStatement("not a short")
  }

  "Long" ! check(toAndFrom[Long])
  "Long failures" ! {
    fromJson[Long](JsString("abc")) must containJsFailureStatement("not a long")
    fromJson[Long](JsNull) must containJsFailureStatement("not a long")
    fromJson[Long](JsBoolean(true)) must containJsFailureStatement("not a long")
    fromJson[Long](JsArray(Nil)) must containJsFailureStatement("not a long")
    fromJson[Long](JsObject(Nil)) must containJsFailureStatement("not a long")
    fromJson[Long](JsNumber(1.23)) must containJsFailureStatement("not a long")
  }

  "Float" ! check(toAndFrom[Float])
  "Float failures" ! {
    fromJson[Float](JsString("abc")) must containJsFailureStatement("not a float")
    fromJson[Float](JsNull) must containJsFailureStatement("not a float")
    fromJson[Float](JsBoolean(true)) must containJsFailureStatement("not a float")
    fromJson[Float](JsArray(Nil)) must containJsFailureStatement("not a float")
    fromJson[Float](JsObject(Nil)) must containJsFailureStatement("not a float")
  }

  "Double" ! check(toAndFrom[Double])
  "Double failures" ! {
    fromJson[Double](JsString("abc")) must containJsFailureStatement("not a double")
    fromJson[Double](JsNull) must containJsFailureStatement("not a double")
    fromJson[Double](JsBoolean(true)) must containJsFailureStatement("not a double")
    fromJson[Double](JsArray(Nil)) must containJsFailureStatement("not a double")
    fromJson[Double](JsObject(Nil)) must containJsFailureStatement("not a double")
  }

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
    fromJson[Map[String, String]](JsObject(Seq("abc" -> JsNumber(123)))) must containJsFieldFailure("abc", "not a string")
  }

  "String" ! check(toAndFrom[String])
  "String failures" ! {
    fromJson[String](JsObject(Nil)) must containJsFailureStatement("not a string")
    fromJson[String](JsNull) must containJsFailureStatement("not a string")
    fromJson[String](JsArray(Nil)) must containJsFailureStatement("not a string")
    fromJson[String](JsBoolean(false)) must containJsFailureStatement("not a string")
    fromJson[String](JsNumber(1.23)) must containJsFailureStatement("not a string")
  }

  import scala.collection.mutable
  "List[String]" ! check(toAndFrom[List[String]])
  "List[Int]" ! check(toAndFrom[List[Int]])
  "Seq[String]" ! check(toAndFrom[Seq[String]])
  "Stream[String]" ! check(toAndFrom[Stream[String]])
  "mutable.Set[String]" ! check(toAndFrom[mutable.Set[String]])
  "Traversable failures" ! {
    fromJson[List[String]](JsObject(Nil)) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsNull) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsString("abc")) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsBoolean(false)) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsNumber(1.23)) must containJsFailureStatement("not an array")
    fromJson[List[String]](JsArray(Seq(JsBoolean(false)))) must containJsFailureStatement("not a string")
    fromJson[List[String]](JsArray(Seq(JsNumber(1.23), JsBoolean(false)))) must containJsFailureStatement("not a string")
    fromJson[mutable.Set[String]](JsArray(Seq(JsNumber(1.23), JsBoolean(false)))) must containJsFailureStatement("not a string")
  }

  "Array[String]" ! check(toAndFrom[Array[String]])
  "Array[Int]" ! check(toAndFrom[Array[Int]])
  "Array failures" ! {
    fromJson[Array[String]](JsObject(Nil)) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsNull) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsString("abc")) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsBoolean(false)) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsNumber(1.23)) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsArray(Seq(JsBoolean(false)))) must containJsFailureStatement("not a string")
  }

  "Option[Int]" ! check(toAndFrom[Option[Int]])
  "Option[String]" ! check(toAndFrom[Option[String]])
  "Option[Map[String, List[Int]]]" ! check(toAndFrom[Option[Map[String, List[Int]]]])
  "Option failures" ! {
    fromJson[Option[Int]](JsString("abc")) must containJsFailureStatement("not an int")
  }

  "NonEmptyList[Int]" ! check(toAndFrom[NonEmptyList[Int]])
  "NonEmptyList failures" ! {
    fromJson[NonEmptyList[String]](JsObject(Nil)) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsNull) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsString("abc")) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsBoolean(false)) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsNumber(1.23)) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsArray(Seq(JsBoolean(false)))) must containJsFailureStatement("not a string")
    fromJson[NonEmptyList[String]](JsArray(Nil)) must containJsFailureStatement("not a non-empty array")
  }

  "NonEmptyList[JsFailure]" ! check(toAndFrom[NonEmptyList[JsFailure]])

  def toAndFrom[T : Reads : Writes : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = toJson(o)
    val read = fromJson[T](wrote)
    read must beSuccess(o)
  }
}
