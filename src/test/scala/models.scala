package jsonz
import org.scalacheck.{Arbitrary, Gen}
import scalaz._
import scalaz.syntax.apply._

package object models {
  import DefaultWrites._
  import DefaultReads._

  import Arbitrary._

  case class Name(first: String, last: String)
  case class Person(name: Name, age: Int)
  val person = Person(Name("Luke", "Amdor"), 28)

  implicit val nameArb = Arbitrary {
    for {
      first <- arbitrary[String]
      last <- arbitrary[String]
    } yield Name(first, last)
  }

  implicit val personArb = Arbitrary {
    for {
      name <- arbitrary[Name]
      age <- arbitrary[Int]
    } yield Person(name, age)
  }

  implicit val nameWrites = new Format[Name] {
    def writes(n: Name) = JsObject {
      "first" -> Jsonz.toJson(n.first) ::
      "last" -> Jsonz.toJson(n.last) ::
      Nil
    }

    def reads(js: JsValue) =
      (field[String]("first", js) |@| field[String]("last", js)) { Name }
  }

  implicit val personFormat = new Format[Person] {
    def writes(p: Person) = JsObject {
      "name" -> Jsonz.toJson(p.name) ::
      "age" -> Jsonz.toJson(p.age) ::
      Nil
    }

    def reads(js: JsValue) =
      (field[Name]("name", js) |@| field[Int]("age", js)) { Person }
  }



  implicit lazy val arbJsValue: Arbitrary[JsValue] = Arbitrary {
    val genJsNull = Gen.value(JsNull)
    val genJsBoolean = for { b <- arbitrary[Boolean] } yield JsBoolean(b)
    val genJsString = for { s <- arbitrary[String] } yield JsString(s)
    val genJsNumber = for { bd <- arbitrary[Float] } yield JsNumber(bd)

    def genJsArray(size: Int) = for {
      n  <- Gen.choose(0, size / 10)
      xs <- Gen.listOfN(n, arbitrary[JsValue])
    } yield JsArray(xs)

    def genJsObject(size: Int) = for {
      n  <- Gen.choose(0, size / 25)
      fields <- Gen.listOfN(n, arbitrary[Pair[String, JsValue]])
    } yield JsObject(fields)

    Gen.sized { size =>
      Gen.oneOf(
        genJsNull,
        genJsBoolean,
        genJsString,
        genJsNumber,
        genJsArray(size),
        genJsObject(size)
      )
    }
  }

}
