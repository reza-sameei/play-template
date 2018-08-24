package model.hard

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