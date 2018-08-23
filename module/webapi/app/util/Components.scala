package util

import com.typesafe.config.Config
import play.api.ApplicationLoader.Context
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.{BuiltInComponentsFromContext, Logger}
import play.controllers.AssetsComponents
import play.filters.HttpFiltersComponents

class Components(
    context: Context
) extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents // https://www.playframework.com/documentation/2.6.x/Migration26#compile-time-default-filters
    with AssetsComponents // https://www.playframework.com/documentation/2.6.x/Migration26#assets
    // with HttpErrorHandlerComponents
{

    val logger = Logger("joon-sik.component-loader")

    logger debug "Trying"

    override def config() : Config = context.initialConfiguration.underlying

    lazy override val httpErrorHandler =
        new ErrorHandler("application.error-hanler")

    lazy val extraEC =
        new ExtraExecutionContextImplV1(actorSystem)

    object controllerInstance {
        lazy val info = new controller.Info(extraEC, controllerComponents, assets())
    }



    lazy override val router = new SimpleRouter {
        override def routes : Routes =
            route.Service.withIn("/service/api/v1", controllerInstance.info).orElse {
                route.PublicStaticAssets.withIn("public", assets)
            }
    }

    logger debug "Done!"
}


object Components {

    /*
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

        lazy val repoOfNetEvent =
            new NetEventRepo("joon-sik.repo.net-event", "netevent_v1", cassandra, extraEC)

        lazy val collector =
            new Collector("joon-sik.controller.collector", repoOfNetEvent, extraEC, controllerComponents)

        lazy val info =
            new Info(extraEC, controllerComponents)
    */

    /*
        lazy override val router =
            Router.empty
            or
            Router.from {
                case POST(p"/alpha/joon-sik/v1/${device}") => collector.bulk(device)
                case PUT(p"/alpha/joon-sik/v1/${device}/${long(time)}") => collector.single(device, time)
            }
            or
            new ApplicationRoutes(info, collector).withPrefix("/base-url/v1").routes
        */



}