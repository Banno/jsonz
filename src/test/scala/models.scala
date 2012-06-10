package jsonz
import org.scalacheck.{Arbitrary, Gen}
import scalaz._
import scalaz.syntax.apply._

package object models {
  import DefaultWrites._
  import DefaultReads._
  import Fields._

  import Arbitrary._

  case class Name(first: String, middle: Option[String], last: String)
  case class Person(name: Name, age: Int)
  val person = Person(Name("Luke", None, "Amdor"), 28)

  implicit val nameArb = Arbitrary {
    for {
      first <- arbitrary[String]
      middle <- arbitrary[Option[String]]
      last <- arbitrary[String]
    } yield Name(first, middle, last)
  }

  implicit val personArb = Arbitrary {
    for {
      name <- arbitrary[Name]
      age <- arbitrary[Int] suchThat (i => i >= 0 && i <= 200)
    } yield Person(name, age)
  }

  implicit val nameWrites = new Format[Name] {
    def writes(n: Name) = JsObject {
      "first" -> Jsonz.toJson(n.first) ::
      "middle" -> Jsonz.toJson(n.middle) ::
      "last" -> Jsonz.toJson(n.last) ::
      Nil
    }

    def allAlphaChars(str: String): Validation[String, String] =
      if (str.forall(_.isLetter)) {
        Success(str)
      } else {
        Failure("not valid chars")
      }

    def reads(js: JsValue) =
      (fieldWithValidation[String]("first", allAlphaChars, js) |@| field[Option[String]]("middle",js) |@| fieldWithValidation[String]("last", allAlphaChars, js)) { Name }
  }

  implicit val personFormat = new Format[Person] {
    def writes(p: Person) = JsObject {
      "name" -> Jsonz.toJson(p.name) ::
      "age" -> Jsonz.toJson(p.age) ::
      Nil
    }

    def validAge(age: Int): ValidationNEL[String, Int] = {
      if (age < 0) {
        Failure("less than zero").toValidationNel
      } else if (age > 200) {
        Failure("too old").toValidationNel
      } else {
        Success(age).toValidationNel
      }
    }

    def reads(js: JsValue) =
      (field[Name]("name", js) |@| fieldWithValidationNel[Int]("age", validAge, js)) { Person }
  }


  implicit def arbSeq[A : Arbitrary]: Arbitrary[Seq[A]] = Arbitrary {
    Gen.sized { size =>
      for {
        ls <- Gen.listOfN(size, arbitrary[A])
      } yield ls.toSeq
    }
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
