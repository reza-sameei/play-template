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
    info: controller.Info
) extends SimpleRouter {
    override def routes: Router.Routes = {
        case GET(p"/") => info.summary
        /*case PUT(p"/${device}") => ??? // contollerName.actionName(device)
        case PUT(p"/${device}/${long(time)}") => ??? // collector.single(device, time)*/
    }
}

object Service {
    def withIn(prefix: String, info: controller.Info): Router.Routes = new Service(info).withPrefix(prefix).routes
}

