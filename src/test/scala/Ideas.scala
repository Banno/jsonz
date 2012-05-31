package jsonz
import org.scalacheck.{Arbitrary, Gen}
import scalaz._
import scalaz.syntax.apply._

object Ideas extends JsonzSpec {
  import DefaultWrites._
  import DefaultReads._

  case class Name(first: String, last: String)
  case class Person(name: Name, age: Int)
  val person = Person(Name("Luke", "Amdor"), 28)

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

  "can write a person" in {
    val js = Jsonz.toJson(person)
    js must beLike {
      case JsObject(("name", JsObject(("first", JsString(first)) :: ("last", JsString(last)) :: Nil)) ::
                    ("age", JsNumber(age)) ::
                    Nil) =>
        first must_== "Luke"
        last must_== "Amdor"
        age must_== 28
    }
    val jsStr = Jsonz.stringify(js)
    jsStr must not beEmpty
  }

  "be able to read raw json to a Person" in {
    val jsStr = """{"name":{"first":"Luke","last":"Amdor"},"age":28}"""
    val js = Jsonz.parse(jsStr)
    js must beLike {
      case JsObject(("name", JsObject(("first", JsString(first)) :: ("last", JsString(last)) :: Nil)) ::
                    ("age", JsNumber(age)) ::
                    Nil) =>
        first must_== "Luke"
        last must_== "Amdor"
        age must_== 28
    }

    val pV = Jsonz.fromJson[Person](js)
    pV must_== Success(person)
  }



  "applicative builders" in {
    import Scalaz._

    val jso = JsObject("k" -> JsString("v") :: Nil)

    // for {
    //   f <- field_c[String]("k")
    // } yield (f |@| f)

    success
  }
}
