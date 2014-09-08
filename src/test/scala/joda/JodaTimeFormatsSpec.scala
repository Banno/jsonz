package jsonz.joda
import jsonz._
import org.joda.time.{DateTimeZone, DateTime}
import org.specs2.ScalaCheck
import org.scalacheck.{Gen, Arbitrary, Prop}
import java.util.Date

object JodaTimeFormatsSpec extends JsonzSpec with ScalaCheck with JodaTimeFormats {
  import Jsonz._

  def utc(time: DateTime) = time.withZone(DateTimeZone.UTC)

  implicit val arbitraryDate: Arbitrary[DateTime] = Arbitrary {
    for {
      d <- Arbitrary.arbitrary[Date] if d.getYear >= 0 && d.getYear <= 9999
    } yield new DateTime(d, DateTimeZone.UTC)
  }

  "DateTimeFormat" should {
    "read from ISO8601 format" in {
      check(toAndFrom[DateTime])
    }

    "read from YYYY-MM-dd format" in {
      check(transformFirst[DateTime, JsString](dt => JsString(dt.toString("YYYY-MM-dd"))))
    }

    "read from long format" in {
      check(transformFirst[DateTime, JsNumber](dt => JsNumber(dt.getMillis)))
    }
  }

  def transformFirst[T: Arbitrary : Reads, X <: JsValue](f: T => X) = Prop.forAll { (o: T) =>
    val after = f(o)
    val read = fromJson[T](after)
    read.map(f(_)) must beSuccessful(after)
  }

  def toAndFrom[T : Reads : Writes : Arbitrary] = Prop.forAll { (o: T) =>
    val wrote = toJson(o)
    val read = fromJson[T](wrote)
    read must beSuccessful(o)
  }
}
