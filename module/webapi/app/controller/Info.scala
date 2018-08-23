package xyz.sigmalab.template.play.controller

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import xyz.sigmalab.template.play.util.ExtraExecutionContext

class Info @Inject() (
    extraEC: ExtraExecutionContext,
    components: ControllerComponents
) extends AbstractController(components) {
    def summary = Action { req => 
        Ok("Kahdoon :)")
    }    
}