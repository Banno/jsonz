package jsonz
import scalaz.{Failure, Success, Validation, ValidationNel}

trait ValidationFormats extends DefaultFormats {
  import Jsonz._

  implicit def ValidationWrites[E: Writes, A: Writes]: Writes[Validation[E, A]] = new Writes[Validation[E, A]] {
    def writes(validation: Validation[E, A]) = validation match {
      case Success(v) => toJson(v)
      case Failure(v) => toJson(v)
    }
  }

  // implicit def ValidationNelWrites[E: Writes, A: Writes]: Writes[ValidationNel[E, A]] = new Writes[ValidationNel[E, A]] {
  //   def writes(validation: ValidationNel[E, A]) = validation match {
  //     case Success(v) => toJson(v)
  //     case Failure(v) => toJson(v.list.map(toJson(_)))
  //   }
  // }
}
