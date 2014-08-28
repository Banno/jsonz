package jsonz.joda
import scalaz.Validation._
import jsonz._
import jsonz.Jsonz._
import jsonz.JsFailure._
import jsonz.DefaultFormats._
import org.joda.time._
import org.joda.time.format._

trait JodaTimeFormats {
  private val utcTimeZone = DateTimeZone.UTC
  private val isoFormat = ISODateTimeFormat.dateTime().withZone(utcTimeZone)
  private val isoFormatNoMillis = ISODateTimeFormat.dateTimeNoMillis().withZone(utcTimeZone)
  private val yyyyMMddFormat = DateTimeFormat.forPattern("yyyy-MM-dd").withZone(utcTimeZone)
  private val rfc1123Format = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZone(utcTimeZone)

  implicit val dateTimeFormat: Format[DateTime] = new Format[DateTime] {
    def writes(dateTime: DateTime) = toJson(isoFormat.print(dateTime))

    def reads(js: JsValue) = js match {
      case JsString(DateTimeStr(datetime)) => success(datetime)
      case JsNumber(DateTimeNum(datetime)) => success(datetime)
      case _                               => jsFailureValidationNel("not a valid datetime")
    }

    object DateTimeStr {
      def unapply(str: String): Option[DateTime] = {
        val os = Some(str)
        os.flatMap {s => try Some(new DateTime(s.toLong, utcTimeZone)) catch { case _: Throwable => None } } orElse
        os.flatMap {s => try Some(yyyyMMddFormat.parseDateTime(s)) catch { case _: Throwable => None } } orElse
        os.flatMap {s => try Some(rfc1123Format.parseDateTime(s)) catch { case _: Throwable => None } } orElse
        os.flatMap {s => try Some(isoFormatNoMillis.parseDateTime(s)) catch { case _: Throwable => None } } orElse
        os.flatMap {s => try Some(isoFormat.parseDateTime(s)) catch { case _: Throwable => None } }
      }
    }

    object DateTimeNum {
      def unapply(num: BigDecimal): Option[DateTime] = Some(new DateTime(num.toLong, utcTimeZone))
    }
  }
}
object JodaTimeFormats extends JodaTimeFormats
