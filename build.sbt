
import xyz.sigmalab.privatebuildhelper.Context

val context = Context(
    organization = "xyz.sigmalab.template",
    originName = "play-template",
    originVersion = "0.1.0-SNAPSHOT",
)

val webapi = context.newModule("webapi", "play-template-webapi")

val root = context.defRoot(webapi)


