package object jsonz {
  type JsonzValidation[+X] = scalaz.ValidationNel[JsFailure, X]

  // Type inequalities
  // We currently do not have shapeless as an explicit dependency in this project.
  // In order to not reqire it we've copied out the type inequality portion.
  // All credit is given to it's original author(s) over in the original repo.
  // https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/package.scala
  trait =:!=[A, B]

  implicit def neq[A, B] : A =:!= B = new =:!=[A, B] {}
  implicit def neqAmbig1[A] : A =:!= A = unexpected
  implicit def neqAmbig2[A] : A =:!= A = unexpected

  def unexpected : Nothing = sys.error("Unexpected invocation")
}
