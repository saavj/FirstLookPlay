package services.database

import com.google.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

case class OwnerRecord(id: Int, petId: Int, firstName: String, lastName: String, firstLine: String, town: String, postcode: String, phoneNumber: String)

class OwnerRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import dbConfig.driver.api._

  val owners = TableQuery[OwnerTable]

  class OwnerTable(tag: Tag) extends Table[OwnerRecord](tag, "OWNER") {

    def * = (id, petId, firstName, lastName, firstLine, town, postcode, phoneNumber) <> ((OwnerRecord.apply _).tupled, OwnerRecord.unapply)

    def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)

    def petId = column[Int]("PET_ID")

    def firstName = column[String]("FIRST_NAME")

    def lastName = column[String]("LAST_NAME")

    def firstLine = column[String]("FIRST_LINE")

    def town = column[String]("TOWN")

    def postcode = column[String]("POSTCODE")

    def phoneNumber = column[String]("PHONE_NUMBER")

  }

}