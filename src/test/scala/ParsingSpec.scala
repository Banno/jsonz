package jsonz
import java.io.{InputStream, IOException}
import scala.collection.mutable.Queue

object ParsingSpec extends JsonzSpec {
  "catch exceptions" in {
    Jsonz.parse("{{{{{lasjdk alskdj") must containJsFailureStatement("not valid JSON")
    Jsonz.parse("112oi12") must containJsFailureStatement("not valid JSON")
    Jsonz.parse("""{"thing" "1"}""") must containJsFailureStatement("not valid JSON")
    Jsonz.parse(new OnlyReadOnceInputStream) must containJsFailureStatement("problem reading json")
    Jsonz.parse(new MisleadingInputStream) must containJsFailureStatement("problem reading json")
  }

  // We need to fake like we have contents to validate parsing, but then not give them anything after parsing.
  class OnlyReadOnceInputStream extends InputStream {
    private[this] val body = {
      val q = new Queue[Char]()
      q.enqueue('{', '"', 'a', '"', ':', '1', '}')
      q
    }

    def read(): Int = {
      if (body.isEmpty) {
        throw new IOException()
      } else {
        body.dequeue().toInt
      }
    }
  }

  // Give a valid block of json at first, but then swap it out from under ourselves
  class MisleadingInputStream extends InputStream {
    private[this] var firstReadCompleted = false
    private[this] val body = {
      val q = new Queue[Char]()
      q.enqueue('{', '"', 'a', '"', ':', '1', '}')
      q
    }

    def read(): Int = {
      if (body.isEmpty) {
        if (firstReadCompleted) {
          '{'.toInt
        } else {
          throw new IOException()
        }
      } else {
        body.dequeue().toInt
      }
    }
  }
}
