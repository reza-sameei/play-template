package controller

import javax.inject.Inject
import play.api.mvc._
import model.hard._
import format.PlayJSON._
import play.api.libs.json._

class PersonAndMovie @Inject()(
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

    def add() = Action(parse.json[Person]) { req =>

        val person = req.body

        if (personsByID.contains(person.internal.value)) Results.BadRequest("Duplicated PersonID")
        else if (personsByEmail.contains(person.email.value)) Results.BadRequest("Duplicated Email")
        else {
            persons.append(person)
            personsByID = persons.map { i => i.internal.value -> i }.toMap
            personsByEmail = persons.map { i => i.email.value -> i }.toMap
            Results.Created.as("application/json")
        }

    }

}
