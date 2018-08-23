package route

import controllers.Assets
import play.api.routing._
import play.api.routing.sird._

class PublicStaticAssets(assets: Assets) extends SimpleRouter{
    override def routes : Router.Routes = {
        case GET(p"/$file*") => assets at file
    }
}

object PublicStaticAssets {
    def withIn(prefix: String, assets: Assets): Router.Routes =
        new PublicStaticAssets(assets).withPrefix(prefix).routes
}
