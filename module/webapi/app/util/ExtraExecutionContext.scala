package util

import javax.inject.Inject

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

trait ExtraExecutionContext extends ExecutionContext

class ExtraExecutionContextImplV1 @Inject() (
    actorSystem: ActorSystem
) extends CustomExecutionContext(actorSystem, "extra")
    with ExtraExecutionContext
