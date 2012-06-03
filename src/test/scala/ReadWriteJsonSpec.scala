package jsonz
import jsonz.models._
import scalaz._

object ReadWriteJsonSpec extends JsonzSpec {
  "can write a person" in check { (person: Person) =>
    val js = Jsonz.toJson(person)
    js must beLike {
      case JsObject(("name", JsObject(("first", JsString(first)) ::
                                      ("last", JsString(last)) ::
                                      Nil)) ::
                    ("age", JsNumber(age)) :: Nil) =>
        first must_== person.name.first
        last must_== person.name.last
        age must_== person.age
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

  "be able to test the validity of fields" in {
    val jsStr = """{"name":{"first":"Luke","last":"Amdor"},"age":-1}"""
    val pV: ValidationNEL[JsFieldFailure, Person] = Jsonz.fromJsonStr[Person](jsStr)
    pV must beLike {
      case Failure(failures) =>
        failures.head must_== JsFieldFailure("age", NonEmptyList("less than zero"))
    }
  }
}
