package jsonz

object ProductFormats extends ProductFormats

trait ProductFormats {
  import Fields._
  import scalaz.syntax.apply._

  def productFormat2[S, T1, T2](field1: String, field2: String)(apply: (T1, T2) => S)(unapply: S => Option[Product2[T1, T2]])(implicit f1: Format[T1], f2: Format[T2]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js))(apply)
  }
}
