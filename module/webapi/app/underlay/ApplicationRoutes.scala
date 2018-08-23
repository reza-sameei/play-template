package xyz.sigmalab.template.play.underlay

import play.api.routing._
import play.api.routing.sird._
import xyz.sigmalab.template.play.controller.Info

class ApplicationRoutes(
    info: Info
) extends SimpleRouter {
    override def routes: Router.Routes = {
        case GET(p"/") => info.summary()
        case PUT(p"/${device}") => ??? // contollerName.actionName(device)
        case PUT(p"/${device}/${long(time)}") => ??? // collector.single(device, time)
    }
}

