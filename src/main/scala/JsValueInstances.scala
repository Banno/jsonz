package jsonz
import scalaz._

trait JsObjectInstances {
  implicit object JsObjectMonoid extends Monoid[JsObject] {
    lazy val zero = JsObject(Nil)
    def append(js: JsObject, other: => JsObject) = js ++ other
  }

  implicit object JsArrayMonoid extends Monoid[JsArray] {
    lazy val zero = JsArray(Nil)
    def append(js: JsArray, other: => JsArray) = JsArray(js.elements ++ other.elements)
  }

  implicit def JsNumberMonoid(implicit m: Monoid[BigDecimal]) = new Monoid[JsNumber] {
    lazy val zero = JsNumber(m.zero)
    def append(js: JsNumber, other: => JsNumber) = JsNumber(js.value + other.value)
  }

  implicit def JsStringMonoid(implicit m: Monoid[String]) = new Monoid[JsString] {
    lazy val zero = JsString(m.zero)
    def append(js: JsString, other: => JsString) = JsString(js.value + other.value)
  }

  implicit object JsBooleanMonoid extends Monoid[JsBoolean] {
    lazy val zero = JsBoolean(true)
    def append(js: JsBoolean, other: => JsBoolean) = JsBoolean(js.value || other.value)
  }
}
