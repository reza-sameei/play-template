package controller

import controllers.Assets
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.routing.Router.Routes
import play.api.routing.{Router, SimpleRouter}
import util.ExtraExecutionContext

class Info @Inject() (
    extraEC: ExtraExecutionContext,
    components: ControllerComponents,
    assets: Assets,
    tm: view.html.Index
) extends AbstractController(components) {

    def summary = Action { req =>
        Ok(tm())
        // Ok("Kahdoon :)")
    }

}