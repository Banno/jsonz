package jsonz
import scalaz._
import scalaz.syntax._

object DefaultReads extends DefaultReads

trait DefaultReads {
  import JsFailure._

  implicit object StringReads extends Reads[String] {
    def reads(js: JsValue) = js match {
      case JsString(str) => Success(str).toValidationNel
      case _ => jsFailureValidationNel("not a string")
    }
  }

  implicit object IntReads extends Reads[Int] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toInt).toValidationNel
      case _ => jsFailureValidationNel("not an int")
    }
  }

  implicit object ShortReads extends Reads[Short] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toShort).toValidationNel
      case _ => jsFailureValidationNel("not a short")
    }
  }

  implicit object LongReads extends Reads[Long] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toLong).toValidationNel
      case _ => jsFailureValidationNel("not a long")
    }
  }

  implicit object FloatReads extends Reads[Float] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toFloat).toValidationNel
      case _ => jsFailureValidationNel("not a float")
    }
  }

  implicit object DoubleReads extends Reads[Double] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num.toDouble).toValidationNel
      case _ => jsFailureValidationNel("not a double")
    }
  }

  implicit object BigDecimalReads extends Reads[BigDecimal] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => Success(num).toValidationNel
      case _ => jsFailureValidationNel("not a arbitrary decimal")
    }
  }

  implicit object BooleanReads extends Reads[Boolean] {
    def reads(js: JsValue) = js match {
      case JsBoolean(bool) => Success(bool).toValidationNel
      case _ => jsFailureValidationNel("not a boolean")
    }
  }

  implicit def mapReads[V](implicit vr: Reads[V]): Reads[Map[String, V]] = new Reads[Map[String, V]] {
    import scalaz.std.list._
    import scalaz.syntax.traverse._

    def reads(js: JsValue) = js match {
      case JsObject(fields) =>

        val fieldsVs: List[ValidationNEL[JsFailure, (String, V)]] = fields.map { case (k,v) =>
          vr.reads(v).bimap(fs => jsFailureNel(k, fs),
                            s => k -> s)
        }.toList

        val overallV: ValidationNEL[JsFailure, List[(String, V)]] =
          fieldsVs.sequence[({type l[a] = ValidationNEL[JsFailure, a]})#l, (String, V)]

        overallV.map(_.toMap)

      case _ => jsFailureValidationNel("not an object")
    }
  }

  implicit def optionReads[T](implicit tr: Reads[T]): Reads[Option[T]] = new Reads[Option[T]] {
    def reads(js: JsValue) = js match {
      case JsNull => Success(None)
      case jsv => tr.reads(jsv).map(Some.apply)
    }
  }

  import scala.collection.generic.CanBuildFrom
  implicit def seqReads[F[_], A](implicit bf: CanBuildFrom[List[_], A, F[A]], ar: Reads[A]) = new Reads[F[A]] {
    import scalaz.std.list._
    import scalaz.syntax.traverse._

    def reads(js: JsValue) = js match {
      case JsArray(elements) =>
        val elementsVs = elements.map(ar.reads).toList
        val overallV: ValidationNEL[JsFailure, List[A]] =
          elementsVs.sequence[({type l[a] = ValidationNEL[JsFailure, a]})#l, A]

        overallV.map { l =>
          val builder = bf()
          builder ++= l
          builder.result
        }
      case _ => jsFailureValidationNel("not an array")
    }
  }

  implicit object JsValueReads extends Reads[JsValue] {
    def reads(js: JsValue) = Success(js).toValidationNel
  }
}
