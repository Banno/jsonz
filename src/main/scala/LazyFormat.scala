package jsonz

trait LazyFormat {
  def lazyFormat[A](f: => Format[A]): Format[A] = new Format[A] {
    lazy val delegate = f
    def writes(a: A) = delegate.writes(a)
    def reads(js: JsValue) = delegate.reads(js)
  }
  def lazyWrites[A](w: => Writes[A]): Writes[A] = new Writes[A] {
    lazy val delegate = w
    def writes(a: A) = delegate.writes(a)
  }
  def lazyReads[A](r: => Reads[A]): Reads[A] = new Reads[A] {
    lazy val delegate = r
    def reads(js: JsValue) = delegate.reads(js)
  }
}
