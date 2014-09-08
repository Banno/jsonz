package jsonz
import org.scalacheck.{Arbitrary, Gen}
import scalaz._
import scalaz.syntax.apply._

package object models {
  import DefaultFormats._
  import Fields._

  import Validation._
  import Arbitrary._

  case class Name(first: String, middle: Option[String], last: String)
  case class Person(name: Name, age: Int)
  val person = Person(Name("Luke", None, "Amdor"), 28)

  object Animals extends Enumeration {
    val Tiger = Value
    val Lion = Value
    val Swallow = Value
  }

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
      age <- Gen.choose(0, 200)
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
        success(str)
      } else {
        failure("not valid chars")
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

    def validAge(age: Int): ValidationNel[String, Int] = {
      if (age < 0) {
        failure("less than zero").toValidationNel
      } else if (age > 200) {
        failure("too old").toValidationNel
      } else {
        success(age).toValidationNel
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

  implicit def arbNel[A: Arbitrary]: Arbitrary[NonEmptyList[A]] = Arbitrary {
    Gen.sized { size =>
      for {
        head <- Arbitrary.arbitrary[A]
        tail <- Gen.listOfN(size, arbitrary[A])
      } yield NonEmptyList.nel(head, tail)
    }
  }

  implicit val genJsNull = Gen.const(JsNull)
  implicit val genJsBoolean = for { b <- arbitrary[Boolean] } yield JsBoolean(b)
  implicit val genJsString = for { s <- arbitrary[String] } yield JsString(s)
  implicit val genJsNumber = for { bd <- arbitrary[Float] } yield JsNumber(bd)

  implicit val arbJsNull = Arbitrary { genJsNull }
  implicit val arbJsBoolean = Arbitrary { genJsBoolean }
  implicit val arbJsString = Arbitrary { genJsString }
  implicit val arbJsNumber = Arbitrary { genJsNumber }

  def genJsArray(size: Int) = for {
    n  <- Gen.choose(0, size / 10)
    xs <- Gen.listOfN(n, arbitrary[JsValue])
  } yield JsArray(xs)

  def genJsObject(size: Int) = for {
    n  <- Gen.choose(0, size / 25)
    fields <- Gen.listOfN(n, arbitrary[(String, JsValue)])
  } yield JsObject(fields)

  implicit lazy val arbJsValue: Arbitrary[JsValue] = Arbitrary {
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

  implicit lazy val arbJsFailure: Arbitrary[JsFailure] = Arbitrary {
    def genJsFailureStatement = for {
      s <- Arbitrary.arbitrary[String]
    } yield JsFailureStatement(s)

    def genJsFieldFailure = for {
      field <- arbitrary[String]
      n  <- Gen.choose(1, 4)
      failures <- Gen.listOfN(n, arbitrary[JsFailure])
    } yield JsFieldFailure(field, NonEmptyList.nel(failures.head, failures.tail))

    Gen.frequency(
      (4, genJsFailureStatement),
      (1, genJsFieldFailure)
    )
  }

  implicit def leftArb[T: Arbitrary]: Arbitrary[Left[T, _]] = Arbitrary {
    for {
      value <- Arbitrary.arbitrary[T]
    } yield Left(value)
  }

  implicit def rightArb[T: Arbitrary]: Arbitrary[Right[T, _]] = Arbitrary {
    for {
      value <- Arbitrary.arbitrary[T]
    } yield Right(value)
  }
}
