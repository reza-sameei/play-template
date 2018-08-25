package util

import play.api.mvc.{Results, Result}

class UnifiedResultGenerator(
    headerKeyForErrors: String,
    genericCntentType: String
) {

    def badReq(message: String):  Result = Results.BadRequest(message).as(genericCntentType)
        .withHeaders(headerKeyForErrors -> message)

    def created(message: String): Result = Results.Created.as(genericCntentType)

}
