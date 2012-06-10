package jsonz
import jsonz.models._
import scalaz._

object ReadWriteJsonSpec extends JsonzSpec {
  "product formats" ! pending

  "can write a person" in check { (person: Person) =>
    val js = Jsonz.toJson(person)
    val middleJson = person.name.middle.map(JsString.apply).getOrElse(JsNull)
    js must beLike {
      case JsObject(("name", JsObject(("first", JsString(first)) ::
                                      ("middle", `middleJson`) ::
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

  "optional field" ! pending

  "be able to read raw json to a Person" in {
    val jsStr = """{"name":{"first":"Luke","middle": null, "last":"Amdor"},"age":28}"""
    val js = Jsonz.parse(jsStr)
    js must beLike {
      case JsObject(("name", JsObject(("first", JsString(first)) :: ("middle", JsNull) :: ("last", JsString(last)) :: Nil)) ::
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
    val jsStr = """{"name":{"first":"123","middle": null, "last":"987"},"age":-1}"""
    val pV: ValidationNEL[JsFailure, Person] = Jsonz.fromJsonStr[Person](jsStr)
    pV must beLike {
      case Failure(failures) =>
        failures.list must contain(JsFieldFailure("age", NonEmptyList(JsFailureStatement("less than zero"))))
        failures.list must contain(JsFieldFailure("name",
                                                  NonEmptyList(JsFieldFailure("first", NonEmptyList(JsFailureStatement("not valid chars"))),
                                                               JsFieldFailure("last", NonEmptyList(JsFailureStatement("not valid chars"))))))
    }
  }
}
