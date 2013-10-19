package jsonz.testkit
import jsonz._
import scalaz.{NonEmptyList, Validation, ValidationNel}
import org.specs2.matcher.{Matcher, MatchersImplicits}

trait Specs2JsonzTestkit extends DefaultFormats with MatchersImplicits {

 def haveJsonField[T](field: Symbol, expected: T)(implicit tr: Reads[T], m: Manifest[T]): Matcher[JsValue] =
    ((js: JsValue) => extract[T](js, field).map(_ == expected) getOrElse false,
     (js: JsValue) => s"""$js \n\thas %s\n\tnot $expected \n\tof type %s""".format(extract[JsValue](js, field).getOrElse(s"'$field' not there!"), m.runtimeClass.getName))

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

  def haveUUIDJsonField(field: Symbol): Matcher[JsValue] =
    ((js: JsValue) => js.asInstanceOf[JsObject].get(field.name).map(isUUID(_)) getOrElse false,
     (js: JsValue) => s"""$js \n\tdoesn't have ${field.name} which is a uuid.""")

  def notHaveJsonField(field: Symbol): Matcher[JsValue] =
    ((js: JsValue) => js.asInstanceOf[JsObject].get(field.name).filter(_ != JsNull).isEmpty,
     (js: JsValue) => s"""$js does have ${field.name} which is non-null""")

  def beSuccessWhich[T](f: (T => Boolean)): Matcher[Validation[_,T]] =
    ((v: Validation[_,T]) => v.exists(f), (v: Validation[_,T]) => s"""$v did not match""")

  def containFailure[A, B](a: A): Matcher[ValidationNel[A, B]] =
    ((v: ValidationNel[A, B]) => v.swap.map(_.list.contains(a)) getOrElse false)

  def haveFailureCount[A, B](n: Int): Matcher[ValidationNel[A, B]] =
    ((v: ValidationNel[A, B]) => v.swap.map(_.list.size == n) getOrElse false)

  private[this] val UUID = """([\da-fA-F]{8}-[\da-fA-F]{4}-[\da-fA-F]{4}-[\da-fA-F]{4}-[\da-fA-F]{12})""".r
  private[this] def isUUID(str: JsValue) = str match {
    case JsString(s) => UUID.findFirstIn(s).isDefined
    case _ => false
  }

  private[this] def extract[T](js: JsValue, field: Symbol)(implicit tr: Reads[T]): Option[T] =
    js.asInstanceOf[JsObject].get(field.name).map(tr.reads).flatMap(_.toOption)
}
