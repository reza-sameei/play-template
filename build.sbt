
import xyz.sigmalab.privatebuildhelper.Context

val context = Context(
    organization = "xyz.sigmalab.template",
    originName = "play-template",
    originVersion = "0.1.0-SNAPSHOT",
)

val jsonext =
    context.newModule("jsonext", "play-template-json-ext")

val webapi =
    context.newModule("webapi", "play-template-webapi")
        .dependsOn(jsonext)

val root = context.defRoot(webapi)
