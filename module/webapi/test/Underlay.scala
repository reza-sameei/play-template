package xyz.sigmalab.template.play.testkit

import akka.stream.ActorMaterializer
import org.scalactic.source.Position
import org.scalatest.{FlatSpec, MustMatchers, OptionValues}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.WsScalaTestClient
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.BuiltInComponents
import play.api.http.Writeable
import play.api.mvc.{Request, Result, Results}
import xyz.sigmalab.template.play.underlay.ApplicationComponents
import play.api.test.Helpers._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

trait Underlay
    extends FlatSpec
    // extends AsyncFlatSpec
    with MustMatchers
    with OptionValues
    with WsScalaTestClient
    with ScalaFutures
    with Results
    with OneAppPerSuiteWithComponents
{

    lazy val underlay = new ApplicationComponents(context)
    override val components: BuiltInComponents = underlay
    implicit val mat = ActorMaterializer()(components.actorSystem)
    implicit val ece = ExecutionContext.global

    def tryBy[T,R](req: => Request[T])(fn: Result => R)(
        implicit
        pos: Position,
        writeable: Writeable[T]
    ): R = {

        val result = route(app, req) match {
            case None => fail(s"Got None in reqsponse of request: ${req}")
            case Some(result) => result
        }

        whenReady(result)(fn)
    }

    val defaultTimeout = 10 seconds

    def await[T](ft: Future[T], timeout: FiniteDuration = defaultTimeout) =
        Await.result(ft, timeout)

    def bodyAsBytes[ByteString](result: Result, timeout: FiniteDuration = defaultTimeout) =
        await(result.body.consumeData, timeout)

    def bodyAsString(result: Result, timeout: FiniteDuration = defaultTimeout) =
        await(result.body.consumeData.map{ i => new String(i.toArray)}, timeout)

}
