package jsonz
import scalaz.{Success, Failure, NonEmptyList, Validation, ValidationNel}
import org.scalacheck.{Arbitrary, Prop}
import scala.language.higherKinds

object ValidationFormatsSpec extends JsonzSpec with ValidationFormats {
  import DefaultFormats._
  import Jsonz._
  import jsonz.models._

  type JsonzFailure[E] = Validation[E, JsValue]

  "write out a Success" ! toAndFrom[({ type l[a] = JsonzValidation[a] })#l, Int](Success.apply(_): Validation[Nothing, Int])

  "write out failures" ! toAndFrom[({ type l[a] = JsonzFailure[a] })#l, Int](Failure.apply(_): Validation[Int, Nothing])

  "write out failure nel's" ! toAndFrom[({ type l[a] = JsonzFailure[a]})#l, NonEmptyList[Int]](n => Failure.apply(n): ValidationNel[Int, Nothing])

  def toAndFrom[M[_], T: Reads : Writes : Arbitrary](builder: T => M[T])(implicit mw: Writes[M[T]]) = Prop.forAll { (o: T) =>
    val wrote = toJson(builder(o))
    val read = fromJson[T](wrote)
    read must beSuccessful(o)
  }
}
