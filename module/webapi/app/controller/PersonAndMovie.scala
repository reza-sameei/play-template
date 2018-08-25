package controller

import javax.inject.Inject
import play.api.mvc._
import model.hard._
import format.PlayJSON._
import play.api.libs.json._
import _root_.util.UnifiedResultGenerator

class PersonAndMovie @Inject()(
    resultGen: UnifiedResultGenerator,
    cc: ControllerComponents
) extends AbstractController(cc){

    val persons = scala.collection.mutable.ListBuffer(
        Person(PersonID(1), NickName("Reza.S"), Email("reza.samee@gmail.com")),
        Person(PersonID(2), NickName("Moein"), Email("moein7nl@gmail.com"))
    )

    var personsByID = persons.map { i => i.internal.value -> i }.toMap
    var personsByEmail = persons.map { i => i.email.value -> i }.toMap


    def list() = Action { req =>
        Ok(Json.toJson(persons))
    }

    def badReq(string: String) = Results.BadRequest(string).withHeaders("X-Error" -> string)

    def add() = Action(parse.json[Person]) { req =>

        val person = req.body

        if (personsByID.contains(person.internal.value)) resultGen.badReq("Duplicated PersonID")
        else if (personsByEmail.contains(person.email.value)) resultGen.badReq("Duplicated Email")
        else {
            persons.append(person)
            personsByID = persons.map { i => i.internal.value -> i }.toMap
            personsByEmail = persons.map { i => i.email.value -> i }.toMap
            resultGen.created("Created!")
        }

    }

    /*
        https://www.playframework.com/documentation/2.6.x/ScalaJsonHttp
        def savePlace = Action(parse.json) { request =>
            val placeResult = request.body.validate[Place]
            placeResult.fold(
                errors => {
                    BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
                },
                place => {
                    Place.save(place)
                    Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
                }
            )
        }
    */

}
