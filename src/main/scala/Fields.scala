package jsonz
import scalaz._

object Fields extends Fields

trait Fields {
  import Validation._
  import JsFailure._

  def field[T](name: String, js: JsValue)(implicit fieldReads: Reads[T], manifest: Manifest[T]): JsonzValidation[T] = js match {
    case jso: JsObject => {
      val maybeFromJson = jso.get(name) map fieldReads.reads
      maybeFromJson.map(groupFieldJsFailures(name)) getOrElse {
        if (classOf[Option[_]].isAssignableFrom(manifest.erasure)) {
          success(None.asInstanceOf[T])
        } else {
          jsFailureValidationNel(name, "is missing")
        }
      }
    }
    case _ => jsFailureValidationNel("is not an object")
  }

  def fieldWithValidationNEL[T : Reads : Manifest](name: String,
                                                   valid: (T) => ValidationNEL[String, T],
                                                   js: JsValue): JsonzValidation[T] =
    field[T](name, js).flatMap { value =>
      valid(value).bimap(fs => jsFailureNel(name, fs.map(jsFailure)), identity)
    }

  def fieldWithValidation[T : Reads : Manifest](name: String, valid: (T) => Validation[String, T], js: JsValue): JsonzValidation[T] =
    fieldWithValidationNEL[T](name, valid andThen (_.toValidationNEL), js)

  private[this] def groupFieldJsFailures[T](name: String)(jsV: JsonzValidation[T]): JsonzValidation[T] =
    jsV.bimap(fs => jsFailureNel(name, fs), identity)

}
