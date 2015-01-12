package jsonz
import jsonz.specs2.Specs2JsonzTestkit

object JsonzBytesCheckSpec extends JsonzSpec with Specs2JsonzTestkit {
  import jsonz._
  import jsonz.DefaultFormats._

  "take a Map, turn it into bytes with jsonz and read out as a string without jsonz" in {
    val map = Map("a" -> List(1), "b" -> List(2,3))
    val bytes = Jsonz.toJsonBytes(map)
    val Some(parsed) = Jsonz.parse(bytes.map(_.toChar).mkString).toOption
    parsed must haveJsonField('a, List(1))
    parsed must haveJsonField('b, List(2,3))
  }

  "take a json object raw string, turn into bytes, and read out via jsonz" in {
    val map = """{"a":[1], "b": [2,3]}"""
    val bytes1 = map.getBytes
    val bytes2 = map.getBytes("UTF-8")
    val Some(parsed1) = Jsonz.fromJsonBytes[Map[String, List[Int]]](bytes1).toOption
    val Some(parsed2) = Jsonz.fromJsonBytes[Map[String, List[Int]]](bytes2).toOption
    parsed1 must contain(exactly("a" -> List(1), "b" -> List(2,3)))
    parsed2 must contain(exactly("a" -> List(1), "b" -> List(2,3)))
  }
}
