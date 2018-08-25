package format

import model.hard.{Movie, Person}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.util.control.NonFatal

object PlayJSON {

    import xyz.sigmalab.template.play.json.JsonOfScalaValueClass._
    implicit val formatOfPerson = Json.format[Person]
    implicit val formatOfMovie = Json.format[Movie]


    /*val writerForPersonID = new Writes[model.PersonID] {
        override def writes(obj : model.PersonID) : JsValue = Json.obj("person_id" -> obj.value)
    }

    val readerForPersonID = new Reads[model.PersonID] {
        override def reads(json : JsValue) : JsResult[model.PersonID] = {
            JsSuccess(model.PersonID((json \ "person_id").as[Long]))
        }
    }*/

    /*val writerForPerson: Writes[model.Person] = (
        (JsPath \ "internal").write[model.PersonID] and
            (JsPath \ "nickname").write[model.NickName] and
            (JsPath \ "email").write[model.Email]
    )(unlift(model.Person.unapply))


    val readerForPerson : Reads[model.Person] = (
        (JsPath \ "internal").read[model.PersonID] and
            (JsPath \ "nickname").read[model.NickName] and
            (JsPath \ "email").read[model.Email]
    )(model.Person.apply _)*/


    /*implicit val formatForPersonID = Json.format[model.hard.PersonID]
    implicit val formatForNickName = Json.format[model.hard.NickName]
    implicit val formatForEmail = Json.format[model.hard.Email]
    implicit val formatForPerson = Json.format[model.hard.Person]

    implicit val formatForMovieID = Json.format[model.hard.MovieID]
    implicit val formatForTitle = Json.format[model.hard.Title]
    implicit val formatYearOfPublish = Json.format[model.hard.YearOfPublish]
    implicit val formatForMovie = Json.format[model.hard.Movie]

    implicit val formatForWatch = Json.format[model.hard.Watched]*/

    /*val formatPerson = Format(
        new Reads[model.hard.Person] {
            override def reads(json : JsValue) : JsResult[Person] = try {
                JsSuccess(model.hard.Person(
                    model.hard.PersonID((json \ "person_id").as[Long]),
                    model.hard.NickName((json \ "name").as[String]),
                    model.hard.Email((json \ "email").as[String])
                ))
            } catch {
                case NonFatal(cause) => JsError(s"Error; can't parse as 'model.hard.Person': ${json}")
            }
        },
        new Writes[model.hard.Person] {
            override def writes(o : Person) : JsValue = Json.obj(
                "person_id" -> o.internal.value,
                "nickname" -> o.name.value,
                "email" -> o.email.value
            )
        }
    )

    val formatMovie = Format(
        new Reads[model.hard.Movie] {
            override def reads(json: JsValue): JsResult[Movie] = try {
                JsSuccess(model.hard.Movie(
                    model.hard.MovieID((json \ "movie_id").as[Long]),
                    model.hard.Title((json \ "title").as[String]),
                    model.hard.YearOfPublish((json \ "year").as[Int])
                ))
            } catch {
                case NonFatal(cause) => JsError(s"Error; can't parse as 'model.hard.Person': ${json}")
            }
        },
        new Writes[model.hard.Movie] {
            override def writes(o : Movie) : JsValue = Json.obj(
                "movie_id" -> o.internal.value,
                "title" -> o.title.value,
                "year" -> o.year.value
            )
        }
    )*/


}
