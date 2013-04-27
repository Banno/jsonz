package object jsonz {
  type JsonzValidation[+X] = scalaz.ValidationNel[JsFailure, X]
}
