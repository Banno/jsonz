package jsonz
import jsonz.models._
import scalaz._

object ReadWriteJsonSpec extends JsonzSpec {
  "can write a person" in prop { (person: Person) =>
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
    jsStr must not (beEmpty)
  }

  "be able to read raw json to a Person" in {
    val jsStr = """{"name":{"first":"Luke","middle": null, "last":"Amdor"},"age":28}"""
    val js = Jsonz.parse(jsStr)
    val pV = Jsonz.fromJson[Person](js.toOption.get)
    pV must beSuccessful(person)
  }

  "allow an optional field not to have to be there" in {
    val jsStr = """{"name":{"first":"Luke", "last":"Amdor"},"age":28}"""
    val pV: JsonzValidation[Person] = Jsonz.fromJsonStr[Person](jsStr)
    (pV.toOption must beSome) and (pV.toOption.flatMap(_.name.middle) must beNone)
  }

  "be able to test the validity of fields" in {
    val jsStr = """{"name":{"first":"123","middle": null, "last":"987"},"age":-1}"""
    val pV: JsonzValidation[Person] = Jsonz.fromJsonStr[Person](jsStr)
    pV must containFailure(JsFieldFailure("age", NonEmptyList(JsFailureStatement("less than zero"))))
    pV must containFailure(JsFieldFailure("name",
                                          NonEmptyList(JsFieldFailure("first", NonEmptyList(JsFailureStatement("not valid chars"))),
                                                       JsFieldFailure("last", NonEmptyList(JsFailureStatement("not valid chars"))))))
  }
}
