package model.hard

case class PersonID(value: PersonID.UnderlayType) extends AnyVal
object PersonID { type UnderlayType = Long }

case class NickName(value: NickName.UnderlayType) extends AnyVal
object NickName { type UnderlayType = String }

case class Email(value: Email.UnderlayType) extends AnyVal
object Email { type UnderlayType = String }

case class Person(internal: PersonID, name: NickName, email: Email)

case class MovieID(value: Long) extends AnyVal
case class Title(value: String) extends AnyVal
case class YearOfPublish(value: Int) extends AnyVal
case class Movie(internal: MovieID, title: Title, year: YearOfPublish)

case class Watched(person: PersonID, movie: MovieID)

case class Date(epoch: Long, zone: String)
case class WatchEvent(person: PersonID, movie: MovieID, date: Date)