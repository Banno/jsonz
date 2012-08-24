package object jsonz {
  type JsonzValidation[X] = scalaz.ValidationNEL[JsFailure, X]
}
