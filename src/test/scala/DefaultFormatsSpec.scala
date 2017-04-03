package jsonz
import jsonz.models._
import scala.language.higherKinds
import org.scalacheck.{Arbitrary, Prop}
import scalaz._

object DefaultFormatsSpec extends JsonzSpec {
  import DefaultFormats._
  import Jsonz._

  "JsValue" ! toAndFrom[JsValue]

  "Int" ! toAndFrom[Int]
  "Int failures" ! {
    fromJson[Int](JsString("abc")) must containJsFailureStatement("not an int")
    fromJson[Int](JsNull) must containJsFailureStatement("not an int")
    fromJson[Int](JsBoolean(true)) must containJsFailureStatement("not an int")
    fromJson[Int](JsArray(Nil)) must containJsFailureStatement("not an int")
    fromJson[Int](JsObject(Nil)) must containJsFailureStatement("not an int")
    fromJson[Int](JsNumber(1.23)) must containJsFailureStatement("not an int")
  }

  "Short" ! toAndFrom[Short]
  "Short failures" ! {
    fromJson[Short](JsString("abc")) must containJsFailureStatement("not a short")
    fromJson[Short](JsNull) must containJsFailureStatement("not a short")
    fromJson[Short](JsBoolean(true)) must containJsFailureStatement("not a short")
    fromJson[Short](JsArray(Nil)) must containJsFailureStatement("not a short")
    fromJson[Short](JsObject(Nil)) must containJsFailureStatement("not a short")
    fromJson[Short](JsNumber(1.23)) must containJsFailureStatement("not a short")
    fromJson[Short](JsNumber(32768)) must containJsFailureStatement("not a short")
  }

  "Long" ! toAndFrom[Long]
  "Long failures" ! {
    fromJson[Long](JsString("abc")) must containJsFailureStatement("not a long")
    fromJson[Long](JsNull) must containJsFailureStatement("not a long")
    fromJson[Long](JsBoolean(true)) must containJsFailureStatement("not a long")
    fromJson[Long](JsArray(Nil)) must containJsFailureStatement("not a long")
    fromJson[Long](JsObject(Nil)) must containJsFailureStatement("not a long")
    fromJson[Long](JsNumber(1.23)) must containJsFailureStatement("not a long")
  }

  "Float" ! toAndFrom[Float]
  "Float failures" ! {
    fromJson[Float](JsString("abc")) must containJsFailureStatement("not a float")
    fromJson[Float](JsNull) must containJsFailureStatement("not a float")
    fromJson[Float](JsBoolean(true)) must containJsFailureStatement("not a float")
    fromJson[Float](JsArray(Nil)) must containJsFailureStatement("not a float")
    fromJson[Float](JsObject(Nil)) must containJsFailureStatement("not a float")
  }

  "Double" ! toAndFrom[Double]
  "Double failures" ! {
    fromJson[Double](JsString("abc")) must containJsFailureStatement("not a double")
    fromJson[Double](JsNull) must containJsFailureStatement("not a double")
    fromJson[Double](JsBoolean(true)) must containJsFailureStatement("not a double")
    fromJson[Double](JsArray(Nil)) must containJsFailureStatement("not a double")
    fromJson[Double](JsObject(Nil)) must containJsFailureStatement("not a double")
  }

  "BigDecimal" ! pending // toAndFrom[BigDecimal] // arithmetic overflows

  "Boolean" ! toAndFrom[Boolean]
  "Boolean failures" ! {
    fromJson[Boolean](JsString("abc")) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsNull) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsArray(Nil)) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsObject(Nil)) must containJsFailureStatement("not a boolean")
    fromJson[Boolean](JsNumber(1.23)) must containJsFailureStatement("not a boolean")
   }

  "Map[String, String]" ! toAndFrom[Map[String, String]]
  "Map[String, Int]" ! toAndFrom[Map[String, Int]]
  "Map[String, Map[String, Int]]" ! toAndFrom[Map[String, Map[String, Int]]]
  "Map failures" ! {
    fromJson[Map[String, String]](JsString("abc")) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsNull) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsArray(Nil)) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsBoolean(false)) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsNumber(1.23)) must containJsFailureStatement("not an object")
    fromJson[Map[String, String]](JsObject(Seq("abc" -> JsNumber(123)))) must containJsFieldFailure("abc", "not a string")
  }

  "String" ! toAndFrom[String]
  "String failures" ! {
    fromJson[String](JsObject(Nil)) must containJsFailureStatement("not a string")
    fromJson[String](JsNull) must containJsFailureStatement("not a string")
    fromJson[String](JsArray(Nil)) must containJsFailureStatement("not a string")
    fromJson[String](JsBoolean(false)) must containJsFailureStatement("not a string")
    fromJson[String](JsNumber(1.23)) must containJsFailureStatement("not a string")
  }

  import scala.collection.mutable
  "List[String]" ! toAndFrom[List[String]]
  "List[Int]" ! toAndFrom[List[Int]]
  "Seq[String]" ! toAndFrom[Seq[String]]
  "Stream[String]" ! toAndFrom[Stream[String]]
  "mutable.Set[String]" ! toAndFrom[mutable.Set[String]]
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

  "Array[String]" ! transformFirst[Array[String], JsArray](items => JsArray(items.map(toJson(_))))
  "Array[Int]" ! transformFirst[Array[Int], JsArray](items => JsArray(items.map(toJson(_))))
  "Array failures" ! {
    fromJson[Array[String]](JsObject(Nil)) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsNull) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsString("abc")) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsBoolean(false)) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsNumber(1.23)) must containJsFailureStatement("not an array")
    fromJson[Array[String]](JsArray(Seq(JsBoolean(false)))) must containJsFailureStatement("not a string")
  }

  "Option[Int]" ! toAndFrom[Option[Int]]
  "Option[String]" ! toAndFrom[Option[String]]
  "Option[Map[String, List[Int]]]" ! toAndFrom[Option[Map[String, List[Int]]]]
  "Option failures" ! {
    fromJson[Option[Int]](JsString("abc")) must containJsFailureStatement("not an int")
  }

  "NonEmptyList[Int]" ! toAndFrom[NonEmptyList[Int]]
  "NonEmptyList failures" ! {
    fromJson[NonEmptyList[String]](JsObject(Nil)) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsNull) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsString("abc")) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsBoolean(false)) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsNumber(1.23)) must containJsFailureStatement("not an array")
    fromJson[NonEmptyList[String]](JsArray(Seq(JsBoolean(false)))) must containJsFailureStatement("not a string")
    fromJson[NonEmptyList[String]](JsArray(Nil)) must containJsFailureStatement("not a non-empty array")
  }

  "NonEmptyList[JsFailure]" ! toAndFrom[NonEmptyList[JsFailure]]

  "Left[Int, _]" ! toAndFrom[({ type l[a] = Left[a, String] })#l, Int](Left.apply)
  "Right[_, Int]" ! toAndFrom[({ type r[a] = Right[String, a] })#r, Int](Right.apply)

  "Left's and Right's should combine powers" ! {
    val model1: Either[Int, String] = Left(100)
    val model2: Either[Int, String] = Right("100")

    fromJson[Either[Int, String]](toJson(model1)).toOption must beSome(model1)
    fromJson[Either[Int, String]](toJson(model2)).toOption must beSome(model2)
  }

  "Left's and Right's can trip us up sometimes" in {
    val model1: Either[Int, String] = Left(100)
    fromJson[Either[String, Int]](toJson(model1)).toOption must beSome(Right(100): Either[String, Int])
    fromJson[Either[Double, Int]](toJson(model1)).toOption must beSome(Left(100D): Either[Double, Int])
  }

  "Left's and Right's shouldn't be able to read other formats though" ! {
    val model1: Either[Int, String] = Left(100)

    fromJson[String](toJson(model1)) must not(beSuccessful)
    fromJson[List[Int]](toJson(model1)) must not(beSuccessful)
    fromJson[Either[Boolean, Boolean]](toJson(model1)) must not(beSuccessful)
    fromJson[Either[List[Int], Map[String, Int]]](toJson(model1)) must not(beSuccessful)
  }

  def transformFirst[T: Arbitrary : Reads, X <: JsValue](f: T => X) = Prop.forAll { (o: T) =>
    val after = f(o)
    val read = fromJson[T](after)
    read.map(f(_)) must beSuccessful(after)
  }

  def toAndFrom[M[_], T: Reads : Writes : Arbitrary](builder: T => M[T])(implicit mw: Writes[M[T]]) = Prop.forAll { (o: T) =>
    val wrote = toJson(builder(o))
    val read = fromJson[T](wrote)
    read must beSuccessful(o)
  }

  def toAndFrom[T : Reads : Writes : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = toJson(o)
    val read = fromJson[T](wrote)
    read must beSuccessful(o)
  }
}
