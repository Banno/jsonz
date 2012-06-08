package jsonz.bench
import com.google.caliper.SimpleBenchmark

case class SimpleModel(first: String, second: String)

object models {
  // TOOD: make a little bit more complicated
  val simple = SimpleModel(first = "111", second = "222")
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

  implicit val SimpleModelFormat = asProduct2("first", "second")(SimpleModel)(SimpleModel.unapply(_).get)

  val jsStr = """{"first" : "111", "second": "222"}"""
  val js = Js(jsStr)

  def timeSerializeStr(reps: Int) = repeat(reps)(tojson(models.simple).toString)
  def timeDeSerializeStr(reps: Int) = repeat(reps)(fromjson[SimpleModel](Js(jsStr)))
  def timeDeSerializeJsValue(reps: Int) = repeat(reps)(fromjson[SimpleModel](js))

  def timeJsParseStr(reps: Int) = repeat(reps)(Js(jsStr))

  def timeToAndFromStr(reps: Int) = repeat(reps)(fromjson[SimpleModel](Js(tojson(models.simple).toString)))
}

class JsonzBench extends SimpleBenchmark with SimpleRepeat {
  import jsonz._
  import Jsonz._
  import Fields._
  import DefaultReads._
  import DefaultWrites._

  implicit val SimpleModelFormat = new Format[SimpleModel] {
    import scalaz.syntax.apply._

    def writes(sm: SimpleModel) = JsObject {
      "first" -> Jsonz.toJson(sm.first) ::
      "second" -> Jsonz.toJson(sm.second) ::
      Nil
    }

    def reads(js: JsValue) =
      (field[String]("first", js) |@| field[String]("second", js)) { SimpleModel }
  }

  val jsStr = """{"first" : "111", "second": "222"}"""

  def timeSerializeStr(reps: Int) = repeat(reps)(toJsonStr(bench.models.simple))
  def timeDeSerializeStr(reps: Int) = repeat(reps)(fromJsonStr[SimpleModel](jsStr))
  def timeToAndFromStr(reps: Int) = repeat(reps)(fromJsonStr[SimpleModel](toJsonStr(bench.models.simple)))
}

class JerksonBench extends SimpleBenchmark with SimpleRepeat {
  import com.codahale.jerkson.Json._

  val jsStr = """{"first" : "111", "second": "222"}"""

  def timeSerializeStr(reps: Int) = repeat(reps)(generate(models.simple))
  def timeDeSerializeStr(reps: Int) = repeat(reps)(parse[SimpleModel](jsStr))
  def timeToAndFromStr(reps: Int) = repeat(reps)(parse[SimpleModel](generate(models.simple)))
}
