package jsonz.bench
import com.google.caliper.SimpleBenchmark

case class NestedModel(nested1: List[Int],
                       nested2: Option[String])

case class SimpleModel(first: String,
                       second: String,
                       third: Option[NestedModel],
                       fourth:  Option[Int])

object models {
  val simple = SimpleModel(first  = "111",
                           second = "222",
                           third  = Some(NestedModel(List(1,2,3), Some("two"))),
                           fourth = Some(4))
  val simpleJsonStr =  """{"first" : "111", "second": "222", "third": {"nested1": [1,2,3], "nested2": null}, "fourth": 9}"""
}

// sjson DONE
// jsonz DONE
// jerkson DONE
// blue eyes
// lift-json
// spray-json

trait SimpleRepeat {
  def repeat[@specialized A](reps: Int)(snippet: => A) = {
    val zero = 0.asInstanceOf[A]
    var i = 0
    var result: A = zero
    while (i < reps) {
      result = snippet
      i += 1
    }
    result
  }
}

package run {
  import com.google.caliper.Runner

  object sjson extends App {
    println("===== sjson =====")
    Runner.main(classOf[SjsonBench], args)
  }

  object jsonz extends App {
    println("===== jsonz =====")
    Runner.main(classOf[JsonzBench], args)
  }

  object jerkson extends App {
    println("===== jerkson =====")
    Runner.main(classOf[JerksonBench], args)
  }
}

class SjsonBench extends SimpleBenchmark with SimpleRepeat {
  import sjson.json._
  import dispatch.json.Js
  import DefaultProtocol._
  import JsonSerialization._

  implicit val NestedModelFormat =
    asProduct2("nested1", "nested2")(NestedModel)(NestedModel.unapply(_).get)
  implicit val SimpleModelFormat =
    asProduct4("first", "second", "third", "fourth")(SimpleModel)(SimpleModel.unapply(_).get)

  val js = Js(models.simpleJsonStr)

  def timeSerializeStr(reps: Int) = repeat(reps)(tojson(models.simple).toString)
  def timeDeSerializeStr(reps: Int) = repeat(reps)(fromjson[SimpleModel](Js(models.simpleJsonStr)))
  def timeDeSerializeJsValue(reps: Int) = repeat(reps)(fromjson[SimpleModel](js))

  def timeJsParseStr(reps: Int) = repeat(reps)(Js(models.simpleJsonStr))

  def timeToAndFromStr(reps: Int) = repeat(reps)(fromjson[SimpleModel](Js(tojson(models.simple).toString)))
}

class JerksonBench extends SimpleBenchmark with SimpleRepeat {
  import com.codahale.jerkson.Json._

  def timeSerializeStr(reps: Int) = repeat(reps)(generate(models.simple))
  def timeDeSerializeStr(reps: Int) = repeat(reps)(parse[SimpleModel](models.simpleJsonStr))
  def timeToAndFromStr(reps: Int) = repeat(reps)(parse[SimpleModel](generate(models.simple)))
}

class JsonzBench extends SimpleBenchmark with SimpleRepeat {
  import jsonz._
  import Jsonz._
  import DefaultFormats._

  implicit val NestedModelFormat =
    productFormat2("nested1", "nested2")(NestedModel.apply)(NestedModel.unapply)
  implicit val SimpleModelFormat =
    productFormat4("first", "second", "third", "fourth")(SimpleModel.apply)(SimpleModel.unapply)

  def timeSerializeStr(reps: Int) = repeat(reps)(toJsonStr(bench.models.simple))
  def timeDeSerializeStr(reps: Int) = repeat(reps)(fromJsonStr[SimpleModel](bench.models.simpleJsonStr))
  def timeToAndFromStr(reps: Int) = repeat(reps)(fromJsonStr[SimpleModel](toJsonStr(bench.models.simple)))
}
