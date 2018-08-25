package route

import play.api.routing._
import play.api.routing.sird._

/**
  *
  * https://www.playframework.com/documentation/2.6.x/ScalaRouting
  * https://www.playframework.com/documentation/2.6.x/ScalaSirdRouter
  *
  * @param info
  */
class Service(
    info: controller.Info,
    personAndMovie: controller.PersonAndMovie
) extends SimpleRouter {
    override def routes: Router.Routes = {
        case GET(p"/") => info.summary
        case GET(p"/person/") => personAndMovie.list()
        case POST(p"/person/") => personAndMovie.add()
        /*case PUT(p"/${device}") => ??? // contollerName.actionName(device)
        case PUT(p"/${device}/${long(time)}") => ??? // collector.single(device, time)*/
    }
}

object Service {
    def withIn(
        prefix: String,
        info: controller.Info,
        personAndMovie: controller.PersonAndMovie
    ): Router.Routes =
        new Service(info, personAndMovie)
            .withPrefix(prefix)
            .routes
}

