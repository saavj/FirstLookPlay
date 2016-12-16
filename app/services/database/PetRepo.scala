package services.database

import com.google.inject.Inject
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

case class PetRecord(id: Int, name: String, dOB: String, notes: String) {
  def toPet: Pet = Pet(id = Some(id), name = Name(name), dob = DOB(dOB), notes = notes)
}

class PetRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import dbConfig.driver.api._

  val pets = TableQuery[PetTable]

  class PetTable(tag: Tag) extends Table[PetRecord](tag, "PET") {

    def * = (id, name, dOB, notes) <> ((PetRecord.apply _).tupled, PetRecord.unapply)

    def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)

    def name = column[String]("NAME")

    def dOB = column[String]("DATE_OF_BIRTH")

    def notes = column[String]("NOTES")
  }

}
