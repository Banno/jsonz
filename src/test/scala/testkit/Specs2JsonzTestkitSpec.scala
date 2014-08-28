package jsonz.specs2
import jsonz._
import org.specs2.mutable.Specification

object Specs2JsonzTestkitSpec extends Specification with Specs2JsonzTestkit {
  import DefaultFormats._

  "The Specs2JsonzTestkit" should {
    "validate on haveJsonField" in {
      val obj = JsObject("name" -> JsString("Adam") :: "address" -> JsNull:: Nil)
      obj must haveJsonField('name, "Adam")
      obj must not(haveJsonField('age, 100))
      obj must haveNullJsonField('address)
    }

    "validate on haveNestedJsonField" in {
      val obj = JsObject("people" -> JsObject(
        "adam" -> JsObject("lastname" -> JsString("Shannon") :: Nil) ::
        "jason" -> JsNull ::
      Nil) :: Nil)

      obj must haveNestedJsonField('people, 'adam, 'lastname)(JsString("Shannon"))
      obj must not(haveNestedJsonField('people, 'adam, 'lastname)(JsNull))
      obj must haveNullNestedJsonField('people, 'jason)
    }

    "validate on haveJsonFieldOfSize" in {
      val obj = JsObject("names" -> JsArray(JsString("adam") :: JsString("jason") :: Nil) :: Nil)
      obj must haveJsonFieldOfSize('names, 2)
    }

    "validate on notHaveJsonField" in {
      val obj = JsObject(Nil)
      obj must not(haveJsonField('name))
    }

    "validate on beSuccessWhich" in {
      val obj = Jsonz.parse("""{"name":"adam"}""")
      obj must beSuccessWhich((j: JsValue) => j.asInstanceOf[JsObject].get("name").map(_ == JsString("adam")) getOrElse false)
    }
  }
}
