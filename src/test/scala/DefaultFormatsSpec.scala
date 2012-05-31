package jsonz
import scalaz.Success
import org.scalacheck.{Arbitrary, Prop}

object DefaultFormatsSpec extends JsonzSpec {
  import DefaultReads._
  import DefaultWrites._
  import DefaultFormats._


  "Int" ! check(toAndFrom[Int])

  "String" ! check(toAndFrom[String])


  def toAndFrom[T : Format : Arbitrary]: Prop = Prop.forAll { (o: T) =>
    val wrote = Jsonz.toJson(o)
    val read = Jsonz.fromJson(wrote)
    read must_== Success(o)
  }
}
