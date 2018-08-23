package xyz.sigmalab.template.play.underlay

import scala.concurrent.{ExecutionContext, Future}

import controllers.Execution
import play.api.{BuiltInComponentsFromContext, Logger}
import play.api.ApplicationLoader.Context
import play.api.http.{HttpEntity, HttpErrorHandler}
import play.api.mvc.{RequestHeader, ResponseHeader, Result}
import play.api.routing.Router
import play.filters.HttpFiltersComponents
// import router.Routes
// import xyz.baikal.alpha.joonsik.controller.{Collector, Info}
// import xyz.baikal.alpha.joonsik.model.NetEventRepo
import xyz.sigmalab.template.play.controller.Info
import xyz.sigmalab.template.play.util.{Cassandra, ExtraExecutionContextImplV1}
import play.api.mvc._
import play.api.routing._
import play.api.routing.sird._

class ApplicationComponents(
    context: Context
) extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents {

    val logger = Logger("joon-sik.component-loader")
    logger debug "Trying"

    lazy val extraEC =
        new ExtraExecutionContextImplV1(actorSystem)

    lazy val cassandra: Cassandra = {

        val configKey = configuration get[String] s"repo.net-event.cassandra-config-key"

        logger debug s"CassandraConfigKey: ${configKey}"

        val config = Cassandra.Config(
            (
                configuration get[String] s"${configKey}.host",
                configuration get[Int] s"${configKey}.port"
            ) :: Nil,
            configuration get[String] s"${configKey}.keyspace",
            configuration get[String] s"${configKey}.username",
            configuration get[String] s"${configKey}.password"
        )

        logger debug s"CassandraConfig: ${config}"

        Cassandra("joon-sik.external.cassandra", config, extraEC)
    }

    /*lazy val repoOfNetEvent =
        new NetEventRepo("joon-sik.repo.net-event", "netevent_v1", cassandra, extraEC)

    lazy val collector =
        new Collector("joon-sik.controller.collector", repoOfNetEvent, extraEC, controllerComponents)

    lazy val info =
        new Info(extraEC, controllerComponents)
    */

    lazy val info = new Info(extraEC, controllerComponents)

    lazy override val httpErrorHandler = new HttpErrorHandler {

        private val logger = Logger("application.error-handler")

        override def onServerError(
            request: RequestHeader,
            cause: Throwable
        ): Future[Result] = {
            logger error (s"Server Error, ${request}", cause)
            Future successful play.api.mvc.Results.InternalServerError.withHeaders("X-Error" -> s"Server Error")
        }

        override def onClientError(
            request: RequestHeader,
            statusCode: Int,
            message: String
        ): Future[Result] = {
            logger warn s"Client Error, ${request}, Response Status Code: ${statusCode}, Message: ${message} ."

            val response =
                Result.apply(ResponseHeader(statusCode), HttpEntity.NoEntity)
                    .withHeaders("X-Error" -> s"${message}")

            Future successful response
        }
    }

    lazy override val router =
        // Router.empty
        /*Router.from {
            case POST(p"/alpha/joon-sik/v1/${device}") => collector.bulk(device)
            case PUT(p"/alpha/joon-sik/v1/${device}/${long(time)}") => collector.single(device, time)
        }*/
        // new ApplicationRoutes(info, collector).withPrefix("/base-url/v1").routes
        new ApplicationRoutes(info).withPrefix("/base-url/v1")

    logger debug "Done!"
}


