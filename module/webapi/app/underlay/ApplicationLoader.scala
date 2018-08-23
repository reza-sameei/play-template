package xyz.sigmalab.template.play.underlay

import play.api.LoggerConfigurator
import play.api.ApplicationLoader.Context

class ApplicationLoader extends play.api.ApplicationLoader {

    def load(context: Context) = {

        // Should be called before returning application instance
        LoggerConfigurator(
            context.environment.classLoader
        ).foreach {
            _.configure(context.environment, context.initialConfiguration, Map.empty)
        }

        // Application instance
        new ApplicationComponents(context).application
    }
}


