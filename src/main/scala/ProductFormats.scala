package jsonz

object ProductFormats extends ProductFormats

trait ProductFormats {
  import Fields._
  import scalaz.typelevel.KNil

  def productFormat1[S, T1](field1: String)(apply: (T1) => S)(unapply: S => Option[T1])(implicit f1: Format[T1], m1: Manifest[T1]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js)).map(apply)
  }


  def productFormat2[S, T1, T2](field1: String, field2: String)(apply: (T1, T2) => S)(unapply: S => Option[Product2[T1, T2]])(implicit f1: Format[T1], f2: Format[T2], m1: Manifest[T1], m2: Manifest[T2]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat3[S, T1, T2, T3](field1: String, field2: String, field3: String)(apply: (T1, T2, T3) => S)(unapply: S => Option[Product3[T1, T2, T3]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3]) = new Format[S] {
    def writes(s: S) = {
      val Some(product) = unapply(s)
      JsObject(Seq(
        field1 -> f1.writes(product._1),
        field2 -> f2.writes(product._2),
        field3 -> f3.writes(product._3)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat4[S, T1, T2, T3, T4](field1: String, field2: String, field3: String, field4: String)(apply: (T1, T2, T3, T4) => S)(unapply: S => Option[Product4[T1, T2, T3, T4]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat5[S, T1, T2, T3, T4, T5](field1: String, field2: String, field3: String, field4: String, field5: String)(apply: (T1, T2, T3, T4, T5) => S)(unapply: S => Option[Product5[T1, T2, T3, T4, T5]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat6[S, T1, T2, T3, T4, T5, T6](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String)(apply: (T1, T2, T3, T4, T5, T6) => S)(unapply: S => Option[Product6[T1, T2, T3, T4, T5, T6]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat7[S, T1, T2, T3, T4, T5, T6, T7](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String)(apply: (T1, T2, T3, T4, T5, T6, T7) => S)(unapply: S => Option[Product7[T1, T2, T3, T4, T5, T6, T7]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat8[S, T1, T2, T3, T4, T5, T6, T7, T8](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8) => S)(unapply: S => Option[Product8[T1, T2, T3, T4, T5, T6, T7, T8]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat9[S, T1, T2, T3, T4, T5, T6, T7, T8, T9](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => S)(unapply: S => Option[Product9[T1, T2, T3, T4, T5, T6, T7, T8, T9]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat10[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => S)(unapply: S => Option[Product10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat11[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => S)(unapply: S => Option[Product11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat12[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => S)(unapply: S => Option[Product12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12]) = new Format[S] {
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
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat13[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => S)(unapply: S => Option[Product13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat14[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => S)(unapply: S => Option[Product14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat15[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => S)(unapply: S => Option[Product15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat16[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => S)(unapply: S => Option[Product16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat17[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String, field17: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => S)(unapply: S => Option[Product17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], f17: Format[T17], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16], m17: Manifest[T17]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16),
        field17 -> f17.writes(product._17)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: field[T17](field17, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat18[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String, field17: String, field18: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => S)(unapply: S => Option[Product18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], f17: Format[T17], f18: Format[T18], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16], m17: Manifest[T17], m18: Manifest[T18]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16),
        field17 -> f17.writes(product._17),
        field18 -> f18.writes(product._18)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: field[T17](field17, js) :^: field[T18](field18, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat19[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String, field17: String, field18: String, field19: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => S)(unapply: S => Option[Product19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], f17: Format[T17], f18: Format[T18], f19: Format[T19], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16], m17: Manifest[T17], m18: Manifest[T18], m19: Manifest[T19]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16),
        field17 -> f17.writes(product._17),
        field18 -> f18.writes(product._18),
        field19 -> f19.writes(product._19)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: field[T17](field17, js) :^: field[T18](field18, js) :^: field[T19](field19, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat20[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String, field17: String, field18: String, field19: String, field20: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => S)(unapply: S => Option[Product20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], f17: Format[T17], f18: Format[T18], f19: Format[T19], f20: Format[T20], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16], m17: Manifest[T17], m18: Manifest[T18], m19: Manifest[T19], m20: Manifest[T20]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16),
        field17 -> f17.writes(product._17),
        field18 -> f18.writes(product._18),
        field19 -> f19.writes(product._19),
        field20 -> f20.writes(product._20)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: field[T17](field17, js) :^: field[T18](field18, js) :^: field[T19](field19, js) :^: field[T20](field20, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat21[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String, field17: String, field18: String, field19: String, field20: String, field21: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => S)(unapply: S => Option[Product21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], f17: Format[T17], f18: Format[T18], f19: Format[T19], f20: Format[T20], f21: Format[T21], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16], m17: Manifest[T17], m18: Manifest[T18], m19: Manifest[T19], m20: Manifest[T20], m21: Manifest[T21]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16),
        field17 -> f17.writes(product._17),
        field18 -> f18.writes(product._18),
        field19 -> f19.writes(product._19),
        field20 -> f20.writes(product._20),
        field21 -> f21.writes(product._21)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: field[T17](field17, js) :^: field[T18](field18, js) :^: field[T19](field19, js) :^: field[T20](field20, js) :^: field[T21](field21, js) :^: KNil).applyP(apply.curried)
  }

  def productFormat22[S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](field1: String, field2: String, field3: String, field4: String, field5: String, field6: String, field7: String, field8: String, field9: String, field10: String, field11: String, field12: String, field13: String, field14: String, field15: String, field16: String, field17: String, field18: String, field19: String, field20: String, field21: String, field22: String)(apply: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => S)(unapply: S => Option[Product22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]])(implicit f1: Format[T1], f2: Format[T2], f3: Format[T3], f4: Format[T4], f5: Format[T5], f6: Format[T6], f7: Format[T7], f8: Format[T8], f9: Format[T9], f10: Format[T10], f11: Format[T11], f12: Format[T12], f13: Format[T13], f14: Format[T14], f15: Format[T15], f16: Format[T16], f17: Format[T17], f18: Format[T18], f19: Format[T19], f20: Format[T20], f21: Format[T21], f22: Format[T22], m1: Manifest[T1], m2: Manifest[T2], m3: Manifest[T3], m4: Manifest[T4], m5: Manifest[T5], m6: Manifest[T6], m7: Manifest[T7], m8: Manifest[T8], m9: Manifest[T9], m10: Manifest[T10], m11: Manifest[T11], m12: Manifest[T12], m13: Manifest[T13], m14: Manifest[T14], m15: Manifest[T15], m16: Manifest[T16], m17: Manifest[T17], m18: Manifest[T18], m19: Manifest[T19], m20: Manifest[T20], m21: Manifest[T21], m22: Manifest[T22]) = new Format[S] {
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
        field12 -> f12.writes(product._12),
        field13 -> f13.writes(product._13),
        field14 -> f14.writes(product._14),
        field15 -> f15.writes(product._15),
        field16 -> f16.writes(product._16),
        field17 -> f17.writes(product._17),
        field18 -> f18.writes(product._18),
        field19 -> f19.writes(product._19),
        field20 -> f20.writes(product._20),
        field21 -> f21.writes(product._21),
        field22 -> f22.writes(product._22)
      ))
    }

    def reads(js: JsValue) =
      (field[T1](field1, js) :^: field[T2](field2, js) :^: field[T3](field3, js) :^: field[T4](field4, js) :^: field[T5](field5, js) :^: field[T6](field6, js) :^: field[T7](field7, js) :^: field[T8](field8, js) :^: field[T9](field9, js) :^: field[T10](field10, js) :^: field[T11](field11, js) :^: field[T12](field12, js) :^: field[T13](field13, js) :^: field[T14](field14, js) :^: field[T15](field15, js) :^: field[T16](field16, js) :^: field[T17](field17, js) :^: field[T18](field18, js) :^: field[T19](field19, js) :^: field[T20](field20, js) :^: field[T21](field21, js) :^: field[T22](field22, js) :^: KNil).applyP(apply.curried)
  }


}
