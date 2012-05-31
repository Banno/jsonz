import scalaz.{NonEmptyList, ValidationNEL}

package object jsonz { // maybe move all this to jsonz.std._
  def field[T: Reads](name: String, js: JsValue): ValidationNEL[JsFieldFailure, T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map Jsonz.fromJson[T]
      maybeFromJson getOrElse fieldFailure(name, "is missing").toValidationNel
    }
    case _ => fieldFailure("", "is not an object").toValidationNel
  }

  // from sjonapp.....
  def field_c[T: Reads](name: String): (JsValue => ValidationNEL[JsFieldFailure, T]) =
    field[T](name, _)


  def fieldFailure(name: String, failure: String) = JsFieldFailure(name, NonEmptyList(failure))
}
