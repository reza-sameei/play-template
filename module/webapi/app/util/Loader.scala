package util

import play.api.ApplicationLoader.Context
import play.api.LoggerConfigurator

class Loader extends play.api.ApplicationLoader {

    def load(context: Context) = {

        // Should be called before returning application instance
        LoggerConfigurator(
            context.environment.classLoader
        ).foreach {
            _.configure(context.environment, context.initialConfiguration, Map.empty)
        }

        // Application instance
        new Components(context).application
    }
}


