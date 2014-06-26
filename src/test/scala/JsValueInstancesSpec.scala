package jsonz
import scalaz._
import jsonz.models._
import org.scalacheck.Arbitrary
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._

object JsValueInstancesSpec extends SpecLite with JsonzSpec with JsValueInstances {
  import scalaz.std.string._
  import scalaz.std.math.bigDecimal._
  import org.scalacheck.Arbitrary._

  implicit def arbJsArray = Arbitrary { genJsArray(100) }
  implicit def arbJsObject = Arbitrary { genJsObject(100) }

  checkAll("JsNull", monoid.laws[JsNull.type])
  checkAll("JsBoolean", monoid.laws[JsBoolean])
  checkAll("JsString", monoid.laws[JsString])
  checkAll("JsNumber", monoid.laws[JsNumber])
  checkAll("JsArray", monoid.laws[JsArray])
  checkAll("JsObject", monoid.laws[JsObject])
}