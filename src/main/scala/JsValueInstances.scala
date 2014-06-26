package jsonz
import scalaz._

trait JsValueInstances extends JsValueEquality {
  implicit object JsNullMonoid extends Monoid[JsNull.type] {
    lazy val zero = JsNull
    def append(js: JsNull.type, other: => JsNull.type) = JsNull
  }

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
    lazy val zero = JsBoolean(false)
    def append(js: JsBoolean, other: => JsBoolean) = JsBoolean(js.value || other.value)
  }
}

trait JsValueEquality {
  import scalaz.std.tuple._
  import scalaz.std.string._
  import scalaz.syntax.equal._

  implicit object JsNullEquals extends Equal[JsNull.type] {
    def equal(js: JsNull.type, other: JsNull.type) = true
  }

  implicit object JsBooleanEquals extends Equal[JsBoolean] {
    def equal(js: JsBoolean, other: JsBoolean) = js.value == other.value
  }

  implicit object JsStringEquals extends Equal[JsString] {
    def equal(js: JsString, other: JsString) = js.value == other.value
  }

  implicit object JsNumberEquals extends Equal[JsNumber] {
    def equal(js: JsNumber, other: JsNumber) = js.value == other.value
  }

  implicit object JsArrayEquals extends Equal[JsArray] {
    def equal(js: JsArray, other: JsArray): Boolean =
      (js.elements.size == other.elements.size) && js.elements.forall(j => other.elements.exists(o => j === o))
  }

  implicit object JsObjectEquals extends Equal[JsObject] {
    def equal(js: JsObject, other: JsObject): Boolean =
      (js.fields.size == other.fields.size) && (js.fields.forall(j => other.fields.exists(o => j === o)))
  }

  implicit val JsValueEquals = new Equal[JsValue] {
    def equal(js: JsValue, other: JsValue): Boolean = (js -> other) match {
      case (JsNull, JsNull) => true
      case (b1: JsBoolean, b2: JsBoolean) => b1 === b2
      case (s1: JsString, s2: JsString) => s1 === s2
      case (n1: JsNumber, n2: JsNumber) => n1 === n2
      case (a1: JsArray, a2: JsArray) => a1 === a2
      case (o1: JsObject, o2: JsObject) => o1 === o2
      case _ => false
    }
  }
}
