package jsonz
import scalaz._
import scalaz.Validation._
import scalaz.syntax._
import scala.language.higherKinds

object DefaultFormats extends DefaultFormats with ProductFormats with LazyFormat with EnumerationFormats

trait DefaultFormats {
  import JsFailure._

  implicit object StringFormat extends Format[String] {
    def reads(js: JsValue) = js match {
      case JsString(str) => success(str).toValidationNel
      case _ => jsFailureValidationNel("not a string")
    }

    def writes(o: String) = JsString(o)
  }

  implicit object ShortFormat extends Format[Short] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) =>
        tryCatchingToJsFailureValidationNel[Short, ArithmeticException]("not a short", num.toShortExact)
      case _ => jsFailureValidationNel("not a short")
    }
    def writes(o: Short) = JsNumber(o)
  }

  implicit object IntFormat extends Format[Int] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) =>
        tryCatchingToJsFailureValidationNel[Int, ArithmeticException]("not an int", num.toIntExact)
      case _ => jsFailureValidationNel("not an int")
    }
    def writes(o: Int) = JsNumber(o)
  }

  implicit object LongFormat extends Format[Long] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) =>
        tryCatchingToJsFailureValidationNel[Long, ArithmeticException]("not a long", num.toLongExact)
      case _ => jsFailureValidationNel("not a long")
    }
    def writes(o: Long) = JsNumber(o)
  }

  implicit object FloatFormat extends Format[Float] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) =>
        tryCatchingToJsFailureValidationNel[Float, ArithmeticException]("not a float", num.toFloat)
      case _ => jsFailureValidationNel("not a float")
    }
    def writes(o: Float) = JsNumber(o)
  }

  implicit object DoubleFormat extends Format[Double] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) =>
        tryCatchingToJsFailureValidationNel[Double, ArithmeticException]("not a double", num.toDouble)
      case _ => jsFailureValidationNel("not a double")
    }
    def writes(o: Double) = JsNumber(o)
  }

  implicit object BigDecimalFormat extends Format[BigDecimal] {
    def reads(js: JsValue) = js match {
      case JsNumber(num) => success(num).toValidationNel
      case _ => jsFailureValidationNel("not an arbitrary decimal")
    }
    def writes(o: BigDecimal) = JsNumber(o)
  }

  implicit object BooleanFormat extends Format[Boolean] {
    def reads(js: JsValue) = js match {
      case JsBoolean(bool) => success(bool).toValidationNel
      case _ => jsFailureValidationNel("not a boolean")
    }
    def writes(o: Boolean) = JsBoolean(o)
  }

  implicit object JsValueFormat extends Format[JsValue] {
    def reads(js: JsValue) = js match {
      case null => success(JsNull).toValidationNel
      case _ => success(js).toValidationNel
    }

    def writes(js: JsValue) = js
  }

  implicit def optionFormat[T](implicit tr: Reads[T], tw: Writes[T], m: Manifest[T]): Format[Option[T]] = new Format[Option[T]] {
    def reads(js: JsValue) = js match {
      case JsNull if classOf[Option[_]].isAssignableFrom(m.runtimeClass) => tr.reads(JsNull).map(Some.apply)
      case JsNull => success(None)
      case jsv => tr.reads(jsv).map(Some.apply)
    }
    def writes(o: Option[T]) = o.map(tw.writes).getOrElse(JsNull)
  }


  implicit def mapFormat[V](implicit vr: Reads[V], vw: Writes[V]): Format[Map[String, V]] = new Format[Map[String, V]] {
    import scalaz.std.list._
    import scalaz.syntax.traverse._

    def reads(js: JsValue) = js match {
      case JsObject(fields) =>

        val fieldsVs: List[JsonzValidation[(String, V)]] = fields.map { case (k,v) =>
          vr.reads(v).bimap(fs => jsFailureNel(k, fs),
                            s => k -> s)
        }.toList

        val overallV: JsonzValidation[List[(String, V)]] =
          fieldsVs.sequence[({type l[a] = JsonzValidation[a]})#l, (String, V)]

        overallV.map(_.toMap)

      case _ => jsFailureValidationNel("not an object")
    }

    def writes(m: Map[String, V]) = JsObject(m.mapValues(v => vw.writes(v)).toSeq)
  }

  import scala.collection.generic.CanBuildFrom
  implicit def traversableFormat[F[_] <: Traversable[_], A](implicit bf: CanBuildFrom[F[_], A, F[A]], ar: Reads[A], aw: Writes[A]): Format[F[A]] = new Format[F[A]] {
    import scalaz.std.list._
    import scalaz.syntax.traverse._

    def reads(js: JsValue) = js match {
      case JsArray(elements) =>
        val elementsVs = elements.map(ar.reads).toList
        val overallV: JsonzValidation[List[A]] =
          elementsVs.sequence[({type l[a] = JsonzValidation[a]})#l, A]

        overallV.map { l =>
          val builder = bf()
          builder ++= l
          builder.result
        }
      case _ => jsFailureValidationNel("not an array")
    }

    def writes(as: F[A]) = JsArray(as.asInstanceOf[Traversable[A]].map(a => aw.writes(a)).toSeq)
  }

  implicit def arrayFormat[T](implicit tr: Reads[T], tw: Writes[T], ev: Manifest[T]) = new Format[Array[T]] {
    import scalaz.std.list._
    import scalaz.syntax.traverse._

    def reads(js: JsValue) = js match {
      case JsArray(elements) =>
        val elementsVs = elements.map(tr.reads).toList
        val overallV: JsonzValidation[List[T]] =
          elementsVs.sequence[({type l[a] = JsonzValidation[a]})#l, T]

        overallV.map(_.toArray)

      case _ => jsFailureValidationNel("not an array")
    }

    def writes(as: Array[T]) = JsArray(as.map(tw.writes).toSeq)
  }

  implicit def nonEmptyListFormat[T](implicit tr: Reads[T], tw: Writes[T], ev: Manifest[T]) = new Format[NonEmptyList[T]] {
    import scalaz.std.list._
    import scalaz.syntax.traverse._

    def reads(js: JsValue) = js match {
      case JsArray(elements) =>
        val elementsVs = elements.map(tr.reads).toList
        val overallV = elementsVs.sequence[({type l[a] = JsonzValidation[a]})#l, T]
        if (overallV.exists(_.isEmpty)) {
          jsFailureValidationNel("not a non-empty array")
        } else {
          overallV.map(lst => NonEmptyList.nel(lst.head, lst.tail))
        }

      case _ => jsFailureValidationNel("not an array")
    }

    def writes(as: NonEmptyList[T]) = JsArray(as.map(tw.writes).list.toSeq)
  }

  private[this] def tryCatchingToJsFailureValidationNel[T, E <: Exception](msg: String, f: => T)(implicit m: Manifest[E]): JsonzValidation[T] = {
    import scala.util.control.Exception._
    val maybeResult = catching(m.runtimeClass).opt(f)
    maybeResult.map(success).getOrElse(jsFailureValidationNel(msg))
  }

  implicit lazy val jsFailureFormat: Format[JsFailure] = new Format[JsFailure] {
    lazy val jsFieldFailureFormat = ProductFormats.productFormat2("field", "failures")(JsFieldFailure.apply)(JsFieldFailure.unapply)

    def reads(jsv: JsValue) = jsv match {
      case JsString(str) => Success(JsFailureStatement(str))
      case jso: JsObject => jsFieldFailureFormat.reads(jso)
      case _             => jsFailureValidationNel("not a failure")
    }

    def writes(failure: JsFailure) = failure match {
      case JsFailureStatement(statement) => JsString(statement)
      case jff: JsFieldFailure           => jsFieldFailureFormat.writes(jff)
    }
  }

  implicit def eitherFormat[L, R](implicit lf: Format[L], rf: Format[R]) = new Format[Either[L, R]] {
    def reads(js: JsValue): JsonzValidation[Either[L, R]] =
      lf.reads(js).map(Left[L, R](_)) orElse rf.reads(js).map(Right[L, R](_))

    def writes(either: Either[L, R]): JsValue = either match {
      case Left(v) => lf.writes(v)
      case Right(v) => rf.writes(v)
    }
  }
}
