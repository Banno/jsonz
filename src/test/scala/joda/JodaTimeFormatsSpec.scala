package jsonz.joda
import jsonz._
import org.joda.time.{DateTimeZone, DateTime}
import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import org.scalacheck.{Gen, Arbitrary}
import java.util.Date
import org.specs2.matcher.{Matcher, MustExpectable}

object JodaTimeFormatsSpec extends Specification with ScalaCheck with JodaTimeFormats {
  import Jsonz._

  def utc(time: DateTime) = time.withZone(DateTimeZone.UTC)

  implicit val arbitraryDate: Arbitrary[DateTime] = Arbitrary {
    for {
      d <- Arbitrary.arbitrary[Date] if d.getYear >= 0 && d.getYear <= 9999
    } yield new DateTime(d, DateTimeZone.UTC)
  }

  "DateTimeFormat" should {
    "read from ISO8601 format" in check { (time: DateTime) =>
      val json = toJson(time)
      val read = fromJson[DateTime](json)
      read.map(_ must_== time).getOrElse(Matcher.failure("not a success", MustExpectable(time)))
    }

    "read from YYYY-MM-dd format" in check { (time: DateTime) =>
      val read = fromJson[DateTime](JsString(time.toString("YYYY-MM-dd")))
      read.map(_ must_== time.withTimeAtStartOfDay).getOrElse(Matcher.failure("not a success", MustExpectable(time.withTimeAtStartOfDay)))
    }

    "read from long format" in check { (time: DateTime) =>
      val read = fromJson[DateTime](JsNumber(time.getMillis))
      read.map(dt => utc(dt) must_== time).getOrElse(Matcher.failure("not a success", MustExpectable(time)))
    }
  }
}
