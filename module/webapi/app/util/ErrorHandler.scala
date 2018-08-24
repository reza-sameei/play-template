package util

import akka.util.ByteString
import play.api.Logger
import play.api.http.{HttpEntity, HttpErrorHandler}
import play.api.mvc.{RequestHeader, ResponseHeader, Result}
import play.api.mvc.Results._

import scala.concurrent.Future

class ErrorHandler(name: String) extends HttpErrorHandler {

    private val logger = Logger(name)

    override def onServerError(
        request : RequestHeader,
        cause : Throwable
    ) : Future[Result] = {
        logger error (s"Server Error, ${request}", cause)
        Future successful InternalServerError.withHeaders("X-Error" -> s"Server Error")
    }

    override def onClientError(
        request : RequestHeader,
        statusCode : Int,
        message : String
    ) : Future[Result] = {
        logger warn s"Client Error, ${request}, Response Status Code: ${statusCode}, Message: ${message} ."

        val response =
            Result.apply(
                ResponseHeader(statusCode),
                HttpEntity.Strict(ByteString("Error"), Some("application/text"))
            ).withHeaders(
                "X-Error" -> s"${message}"
            )

        Future successful response
    }
}