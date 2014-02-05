package jsonz
import scalaz.{Success, Failure, NonEmptyList, Validation, ValidationNel}
import org.scalacheck.{Arbitrary, Prop}

object ValidationFormatsSpec extends JsonzSpec with ValidationFormats {
  import DefaultFormats._
  import Jsonz._

  implicitly[Writes[scalaz.Validation[Nothing,Int]]]

  "write out a Success" ! check(toAndFrom[({ type l[a] = Validation[Nothing, a] })#l, Int](Success.apply(_): Validation[Nothing, Int]))

  // "write out failures" ! check(toAndFrom())

  //"write out failure nel's" ! check(toAndFrom())

  def toAndFrom[M[_], T: Reads : Writes : Arbitrary](builder: T => M[T])(implicit mw: Writes[M[T]]) = Prop.forAll { (o: T) =>
    val wrote = toJson(builder(o))
    val read = fromJson[T](wrote)
    read must beSuccess(o)
  }
}
