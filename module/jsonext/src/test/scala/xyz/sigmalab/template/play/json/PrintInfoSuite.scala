package xyz.sigmalab.template.play.json

import org.scalatest._
import org.scalactic.source.Position
import play.api.libs.json._

case class PersonID(value: Long) extends AnyVal
case class NickName(value: String) extends AnyVal
case class Email(value: String) extends AnyVal
case class Person(internal: PersonID, name: NickName, email: Email)

case class MovieID(value: Long) extends AnyVal
case class Title(value: String) extends AnyVal
case class YearOfPublish(value: Int) extends AnyVal
case class Movie(internal: MovieID, title: Title, year: YearOfPublish)

case class Watched(person: PersonID, movie: MovieID)

case class Date(epoch: Long, zone: String)
case class WatchEvent(person: PersonID, movie: MovieID, date: Date)

case class PersonName(value: String)

class PrintInfoSuite extends FlatSpec with MustMatchers {

    it must "check anyval subtypeing at compile-time" in {

        "PrintInfo.of[PersonName]" mustNot compile

        "PrintInfo.of[PersonID]" must compile

    }

    it must "X" in {

        val writer = PrintInfo.of[PersonID]

        val json = writer.writes(PersonID(12))

        info(play.api.libs.json.Json.prettyPrint(json))

    }

    it must "Write & Read" in {

        val writer = JsonOfScalaValueClass.writer[PersonID]
        val json = writer.writes(PersonID(12))
        info(play.api.libs.json.Json.prettyPrint(json))

        val reader = JsonOfScalaValueClass.reader[PersonID]
        val result = reader.reads(json)
        info(s"${result}")
    }

    it must "Write & Read with Format" in {

        val format = JsonOfScalaValueClass.format[PersonID]

        val json = format.writes(PersonID(12))

        info(play.api.libs.json.Json.prettyPrint(json))

        val result = format.reads(json)
        info(s"${result}")

    }

    it must "Write & Read with Implicits" in {

        import JsonOfScalaValueClass._

        val json = implicitly[Writes[PersonID]].writes(PersonID(12))

        info(play.api.libs.json.Json.prettyPrint(json))

        val result = implicitly[Reads[PersonID]].reads(json)
        info(s"${result}")

    }

    it must "Format Person" in {

        import JsonOfScalaValueClass._
        implicit val formatForPerson = Json.format[Person]

        val origin = Person(PersonID(1), NickName("Reza"), Email("reza.samee@gmail.com"))
        val json = Json.toJson(origin)
        val clone = Json.fromJson[Person](json).get

        info(
            s"""
                 |Origin    : ${origin}
                 |Clone     : ${clone}
                 |areEqual  : ${clone == origin}
                 |JSON      : ${json}
             """.stripMargin)

    }

}
