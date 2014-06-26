package jsonz
import org.scalacheck._

// Used from https://github.com/scalaz/scalaz/blob/scalaz-seven/tests/src/test/scala/scalaz/SpecLite.scala#L7
abstract class SpecLite extends Properties("") {
  def checkAll(name: String, props: Properties) {
    for ((name2, prop) <- props.properties) yield {
      property(name + ":" + name2) = prop
    }
  }
}
