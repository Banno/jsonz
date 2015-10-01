package jsonz
import scalaz.{Validation, ValidationNel}
import scalaz.Validation.FlatMap._

object Fields extends Fields

trait Fields {
  import JsFailure._

  def field[T](name: String, js: JsValue)(implicit fieldReads: Reads[T], manifest: Manifest[T]): JsonzValidation[T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map fieldReads.reads
      maybeFromJson.map(groupFieldJsFailures(name)) getOrElse {
        if (classOf[Option[_]].isAssignableFrom(manifest.runtimeClass)) {
          Validation.success(None.asInstanceOf[T])
        } else {
          jsFailureValidationNel(name, "is missing")
        }
      }
    }
    case _ => jsFailureValidationNel(name, "is not an object")
  }

  def fieldWithValidationNel[T : Reads : Manifest](name: String,
                                                   valid: (T) => ValidationNel[String, T],
                                                   js: JsValue): JsonzValidation[T] =
    field[T](name, js).flatMap { value =>
      valid(value).bimap(fs => jsFailureNel(name, fs.map(jsFailure)), identity)
    }

  def fieldWithValidation[T : Reads : Manifest](name: String, valid: (T) => Validation[String, T], js: JsValue): JsonzValidation[T] =
    fieldWithValidationNel[T](name, valid andThen (_.toValidationNel), js)

  private[this] def groupFieldJsFailures[T](name: String)(jsV: JsonzValidation[T]): JsonzValidation[T] =
    jsV.bimap(fs => jsFailureNel(name, fs), identity)

}
