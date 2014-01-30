package jsonz.testkit
import jsonz._
import org.specs2.matcher.{Matcher, MatchersImplicits}
import scalaz.Validation

trait JsonzTestkit extends DefaultFormats with MatchersImplicits  {
  def getJsArray(js: JsValue, field: String): Seq[JsValue] =
    js.asInstanceOf[JsObject].get(field).get.asInstanceOf[JsArray].elements

  def haveJsonField[T](field: Symbol, expected: T)(implicit tr: Reads[T], m: Manifest[T]): Matcher[JsValue] =
    ((js: JsValue) => extract[T](js, field).map(_ == expected) getOrElse false,
     (js: JsValue) => "%s \n\thas %s\n\tnot %s\n\tof type %s".format(js, extract[JsValue](js, field).getOrElse("'not there!'"), expected, m.runtimeClass.getName))

  def haveJsonFieldOfSize(field: Symbol, expectedSize: Int): Matcher[JsValue] =
    ((js: JsValue) => extract[Seq[JsValue]](js, field).map(_.size == expectedSize).getOrElse(false),
     (js: JsValue) => "%s field %s of %s did not have size %s".format(js, field, extract[Seq[JsValue]](js, field).getOrElse("'not there!'"), expectedSize))

  def beSuccessWhich[T](f: (T => Boolean)): Matcher[Validation[_,T]] =
    ((v: Validation[_,T]) => v.exists(f), (v: Validation[_,T]) => "%s did not match".format(v))

  private[this] def extract[T](js: JsValue, field: Symbol)(implicit tr: Reads[T]): Option[T] =
    js.asInstanceOf[JsObject].get(field.name).map(tr.reads).flatMap(_.toOption)
}
