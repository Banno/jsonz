package jsonz.specs2
import jsonz._
import scalaz.{NonEmptyList, Validation, ValidationNel}
import org.specs2.matcher.{Matcher, MatchersImplicits}

object Specs2JsonzTestkit extends Specs2JsonzTestkit

trait Specs2JsonzTestkit extends MatchersImplicits {
  import DefaultFormats._

 def haveJsonField[T](field: Symbol, expected: T)(implicit tr: Reads[T], m: Manifest[T]): Matcher[JsValue] =
    ((js: JsValue) => extract[T](js, field).map(_ == expected) getOrElse false,
     (js: JsValue) => s"""$js \n\thas %s\n\tnot $expected \n\tof type %s""".format(extract[JsValue](js, field).getOrElse(s"'$field' not there!"), m.runtimeClass.getName))

 def haveJsonField(field: Symbol): Matcher[JsValue] =
    ((js: JsValue) => js match {
       case JsObject(fields) => fields.exists { case (key, _) => key == field }
       case _                => false
     },
     (js: JsValue) => s"""$js does not have field %s""".format(js, field))

  def haveNestedJsonField[T <: JsValue](fields: Symbol*)(expected: T): Matcher[JsValue] =
    ((js: JsValue) => fields.foldLeft(Option(js)) { (acc, f) => acc.flatMap(extract[JsValue](_, f)) } map { _ == expected } getOrElse false,
     (js: JsValue) => s"""$js \n\tdoesn't have nested $fields keys \n\tof value $expected """)

  def haveNullJsonField(field: Symbol): Matcher[JsValue] =
    ((js: JsValue) => js.asInstanceOf[JsObject].get(field.name).map(_ == JsNull) getOrElse false,
     (js: JsValue) => s"""$js \n\tdoesn't have ${field.name} which is null""")

  def haveNullNestedJsonField(fields: Symbol*): Matcher[JsValue] =
    ((js: JsValue) => fields.foldLeft(Option(js)) { (acc, f) => acc.flatMap(extract[JsValue](_, f)) } map { _ == JsNull } getOrElse false,
     (js: JsValue) => s"""$js \n\t doesn't have nested $fields keys \n\twhich is null""")

  def haveJsonFieldOfSize(field: Symbol, expectedSize: Int): Matcher[JsValue] =
    ((js: JsValue) => extract[Seq[JsValue]](js, field).map(_.size == expectedSize).getOrElse(false),
     (js: JsValue) => s"""$js field $field of %s did not have size $expectedSize""".format(extract[Seq[JsValue]](js, field).getOrElse(s"'${field} not there!'")))

  def haveJsonFieldWhichContains[T <: JsValue](field: Symbol, containsField: Symbol, expected: T): Matcher[JsValue] =
    ((js: JsValue) => extract[JsValue](js, field).flatMap(_.asInstanceOf[JsArray].elements.flatMap(_.asInstanceOf[JsObject].get(containsField.name).map(_ == expected)).find(b => b)).getOrElse(false),
     (js: JsValue) => s"""$js \n\tdoesn't have field $field or nested field $containsField""")

  def beSuccessWhich[T](f: (T => Boolean)): Matcher[Validation[_,T]] =
    ((v: Validation[_,T]) => v.exists(f), (v: Validation[_,T]) => s"""$v did not match""")

  def containFailure[A, B](a: A): Matcher[ValidationNel[A, B]] =
    ((v: ValidationNel[A, B]) => v.swap.map(_.list.contains(a)) getOrElse false)

  def haveFailureCount[A, B](n: Int): Matcher[ValidationNel[A, B]] =
    ((v: ValidationNel[A, B]) => v.swap.map(_.list.size == n) getOrElse false)

  private[this] def extract[T](js: JsValue, field: Symbol)(implicit tr: Reads[T]): Option[T] =
    js.asInstanceOf[JsObject].get(field.name).map(tr.reads).flatMap(_.toOption)
}
