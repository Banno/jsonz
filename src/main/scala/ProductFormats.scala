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

  def productFormat3[S, T1, T2, T3](field1: String, field2: String, field3: String)(apply: (T1, T2, T3) => S)(unapply: S => Option[Product3[T1, T2, T3]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js))(apply)
  }

  def productFormat4[S, T1, T2, T3, T4](field1: String, field2: String, field3: String, field4: String)(apply: (T1, T2, T3, T4) => S)(unapply: S => Option[Product4[T1, T2, T3, T4]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js))(apply)
  }

  def productFormat5[S, T1, T2, T3, T4, T5](field1: String, field2: String, field3: String, field4: String, field5: String)(apply: (T1, T2, T3, T4, T5) => S)(unapply: S => Option[Product5[T1, T2, T3, T4, T5]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js))(apply)
  }

  def productFormat6[S, T1, T2, T3, T4, T5, T6](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String)(apply: (T1, T2, T3, T4, T5, T6) => S)(unapply: S => Option[Product6[T1, T2, T3, T4, T5, T6]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js))(apply)
  }

  def productFormat7[S, T1, T2, T3, T4, T5, T6, T7](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String)(apply: (T1, T2, T3, T4, T5, T6, T7) => S)(unapply: S => Option[Product7[T1, T2, T3, T4, T5, T6, T7]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6),
        field7 -> f7.writes(product._7)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js) |@| field[T7](field7, js))(apply)
  }

  def productFormat8[S, T1, T2, T3, T4, T5, T6, T7, T8](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8) => S)(unapply: S => Option[Product8[T1, T2, T3, T4, T5, T6, T7, T8]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6),
        field7 -> f7.writes(product._7),
        field8 -> f8.writes(product._8)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js) |@| field[T7](field7, js) |@| field[T8](field8, js))(apply)
  }

  def productFormat9[S, T1, T2, T3, T4, T5, T6, T7, T8, T9](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => S)(unapply: S => Option[Product9[T1, T2, T3, T4, T5, T6, T7, T8, T9]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6),
        field7 -> f7.writes(product._7),
        field8 -> f8.writes(product._8),
        field9 -> f9.writes(product._9)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js) |@| field[T7](field7, js) |@| field[T8](field8, js) |@| field[T9](field9, js))(apply)
  }

  def productFormat10[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => S)(unapply: S => Option[Product10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6),
        field7 -> f7.writes(product._7),
        field8 -> f8.writes(product._8),
        field9 -> f9.writes(product._9),
        field10 -> f10.writes(product._10)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js) |@| field[T7](field7, js) |@| field[T8](field8, js) |@| field[T9](field9, js) |@| field[T10](field10, js))(apply)
  }

  def productFormat11[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => S)(unapply: S => Option[Product11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6),
        field7 -> f7.writes(product._7),
        field8 -> f8.writes(product._8),
        field9 -> f9.writes(product._9),
        field10 -> f10.writes(product._10),
        field11 -> f11.writes(product._11)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js) |@| field[T7](field7, js) |@| field[T8](field8, js) |@| field[T9](field9, js) |@| field[T10](field10, js) |@| field[T11](field11, js))(apply)
  }

  def productFormat12[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => S)(unapply: S => Option[Product12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3),
        field4 -> f4.writes(product._4),
        field5 -> f5.writes(product._5),
        field6 -> f6.writes(product._6),
        field7 -> f7.writes(product._7),
        field8 -> f8.writes(product._8),
        field9 -> f9.writes(product._9),
        field10 -> f10.writes(product._10),
        field11 -> f11.writes(product._11),
        field12 -> f12.writes(product._12)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) |@| field[T2](field2, js) |@| field[T3](field3, js) |@| field[T4](field4, js) |@| field[T5](field5, js) |@| field[T6](field6, js) |@| field[T7](field7, js) |@| field[T8](field8, js) |@| field[T9](field9, js) |@| field[T10](field10, js) |@| field[T11](field11, js) |@| field[T12](field12, js))(apply)
  }

}
