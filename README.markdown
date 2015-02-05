# jsonz

> A little of sjson, sjsonapp, scalaz and play's JSON libraries mixed together.

jsonz is a library that implements json serialization and deserialization via typeclasses in scala.

# Differences between other libraries

## Default Values

If a model (typically case classes) has default values, they may be omitted from json when reading that json.

```scala
import jsonz._
import jsonz.DefaultFormats._
object DefaultValues {
  case class Foo(thing: Int, other: Option[String] = None)
  implicit val format = productFormat2("thing", "other")(Foo.apply)(Foo.unapply)
  val json = """{"thing": 1}"""
  Jsonz.fromJsonStr[Foo](json)
}
```

## Failures

Jsonz is different about failures. When parsing json failures are accumulated and delivered at the end. The type `JsonzValidation[T]` is a type alias for scalaz's `Validation[NonEmptyList[JsFailure], T]` so the user will receive either a `scalaz.Success` with an instance of the model filled in with the data from the json, or a `scalaz.Failure` that contains failures that occured duirng parsing. These failures could be from missing fields, or malformly typed elements.

### No Exceptions

Jsonz tries really hard to never throw exceptions. Other json libraries we encountered would throw exceptions at malformed or key-missing json. Instead jsonz will return a parsable failure for the user to parse.

# Usage

## Reading and Writing from models (case classes)

Jsonz provides [ProductFormats](src/main/scala/ProductFormats.scala) for use over anything that abstracts over [`scala.Product`](http://www.scala-lang.org/api/current/scala/Product.html). This means your typical case class can be written to json and read back out.

```scala
import jsonz._
import jsonz.DefaultFormats._

object Example {
    case class Foo(thing: Int, other: String)
    implicit val fooFormat = productFormat2("thing", "other")(Foo.apply)(Foo.unapply)

    Jsonz.fromJsonStr[Foo]("""{"thing":12, "other":"hi"}""") // JsonzValidation[Foo]
    Jsonz.toJsonStr(Foo(12, "hi")) // JsValue
}
```
## Out of the box type support

jsonz provides support for reading and writing the following types out of the box: `String`, `Short`, `Int`, `Long`, `Float`, `Double`, `BigDecimal`, `Boolean`, `JsValue`. The following types require a `Format` for the generic types to be in scope: `Option[T]`, `Map[String, V]`, `Traversable[T]`, `Array[T]`, `scalaz.NonEmptyList[T]`, `jsonz.JsFailure`, `Either[L, R]`, and `scalaz.Validation[E, A]`.

### Enumerations

Java's `java.lang.Enum` and Scala's `scala.Enumeration` are supported out of the box, simply mix in (or import the companion object) `jsonz.EnumerationFormats` into the scope where you wish to define the implicit format and call `javaEnumerationFormat` or `scalaEnumerationFormat` with the enumeration instance passed in.

```java
// Defined in Day.java
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}
```

```scala
import jsonz._
import jsonz.DefaultFormats._
object Animals extends Enumeration {
    val Tiger = Value
    val Lion = Value
    val Swallow = Value
}
implicit val AnimalsFormat: Format[Animals.Value] = scalaEnumerationFormat(Animals)
implicit val DaysFormat: Format[Day] = javaEnumerationFormat[Day](Day.MONDAY)
```

### Recursive Values

Given a recursive type you will need to wrap the format in the `lazyFormat` method.

```scala
import jsonz._
import jsonz.DefaultFormats._
case class RecursiveType(rs: List[RecursiveType])
implicit lazy val RecursiveTypeFormat: Format[RecursiveType] = lazyFormat(productFormat1("rs")(RecursiveType.apply)(RecursiveType.unapply))
```

### Joda-Time

Joda-Time is typically the first library chosen for performing date/time operations on the JVM. Jsonz offers support for reading and writing it's `org.joda.time.DateTime` class via the following imports. The formats used to try and parse are: `ISO 8601` (with and without millis), `yyyy-MM-dd`, and [RFC 1123](https://tools.ietf.org/html/rfc1123) (EEE, dd MMM yyyy HH:mm:ss 'GMT').

```scala
import jsonz._
import jsonz.joda.JodaTimeFormats._
```

### Spray

Spray versions 1.1.x, 1.2.x, and 1.3.x are supported to read entit ies and complete requests as json via jsonz. To use the following import will need to be used, or to mixin the associated trait under the same name.

```scala
import jsonz.spray.JsonzMarshalling._ // companion object
import jsonz.spray.JsonzMarshalling   // trait
```

### Specs2

Specs2 matchers are provided by jsonz. In order to use them simply mix in (or import via the companion object) `jsonz.specs2.Specs2JsonzTestkit`. Then the following matchers are visible. For examples refer to the [test over these matchers](src/test/scala/testkit/Specs2JsonzTestkitSpec.scala).

```scala
def haveJsonField[T](field: Symbol, expected: T)(implicit tr: Reads[T], m: Manifest[T]): Matcher[JsValue]
def haveJsonField(field: Symbol): Matcher[JsValue]
def haveNestedJsonField[T <: JsValue](fields: Symbol*)(expected: T): Matcher[JsValue]
def haveNullJsonField(field: Symbol): Matcher[JsValue]
def haveNullNestedJsonField(fields: Symbol*): Matcher[JsValue]
def haveJsonFieldOfSize(field: Symbol, expectedSize: Int): Matcher[JsValue]
def haveJsonFieldWhichContains[T <: JsValue](field: Symbol, containsField: Symbol, expected: T): Matcher[JsValue]
def beSuccessWhich[T](f: (T => Boolean)): Matcher[Validation[_,T]]
def containFailure[A, B](a: A): Matcher[ValidationNel[A, B]]
def haveFailureCount[A, B](n: Int): Matcher[ValidationNel[A, B]]
```

## Custom Types

You can use the methods defined in [`jsonz.Fields`](src/main/scala/Fields.scala) to extract out parts of a json object at a much more fine-grained detail. An exmaple:

```
package api
import scalaz.Success
import jsonz._
import jsonz.Fields._
import jsonz.JsFailure._

case class UserUpdateParams(firstName: Option[String] = None, lastName: Option[String] = None)

object Formats extends DefaultFormats {
  case class NonEmptyString(str: String)
  def nonEmptyStringToString(nes: NonEmptyString): String = nes.str

  implicit val UserUpdateParamsReads: Reads[UserUpdateParams] = new Reads[UserUpdateParams] {
    def reads(js: JsValue): JsonzValidation[UserUpdateParams] = {
      def str(key: String): JsonzValidation[Option[String]] = field[Option[String]](key, js).flatMap {
        case s@Some(str) => if (str.trim.nonEmpty) Success(s) else jsFailureValidationNel("is empty")
        case other => Success(other)
      }

      for {
        firstName <- str("firstName")
        lastName <- str("lastName")
      } yield UserUpdateParams(firstName, lastName)
    }
  }
}
```

## Scalaz Instances

Mixing in the `jsonz.JsValueInstances` (which extends from `jsonz.JsValueEquality`) will provide `scalaz.Monoid` and `scalaz.Equal` instances for `JsValue`'s sub types, but not `JsValue` itself. These are often useful if you want to combine two like subtypes of `JsValue`.

# License

This is released under the Apache License, Version 2.0. See [LICENSE][License]

[License]: LICENSE
