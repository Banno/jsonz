package jsonz.spray
import jsonz.{Jsonz, Writes}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Failure}
import _root_.spray.http._
import _root_.spray.routing.Rejection
import _root_.spray.httpx.marshalling._
import _root_.spray.httpx.unmarshalling._

trait ExtendedSpraySupport
  extends StandardLibrarySpraySupport
  with ScalazSpraySupport

private[spray]
trait StandardLibrarySpraySupport //extends MetaMarshallers

private[spray]
trait ScalazSpraySupport extends StandardLibrarySpraySupport {
  import scalaz._
  import scalaz.effect.IO

  implicit def disjunctionMarshaller[A](implicit marsh: Marshaller[A]): Marshaller[Throwable \/ A] =
    Marshaller[Throwable \/ A] { (value, ctx) =>
      value match {
        case \/-(v) => marsh(v, ctx)
        case -\/(err) => ctx.handleError(err)
      }
    }

  implicit def ioMonadMarshaller[A](implicit m: Marshaller[A]): Marshaller[IO[A]] =
    Marshaller[IO[A]] { (value, ctx) =>
      val crude: Throwable \/ A = value.catchLeft.unsafePerformIO
      val marsh = implicitly[Marshaller[Throwable \/ A]]
      marsh(crude, ctx)
    }
}
