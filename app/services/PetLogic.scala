package services.database

import com.google.inject.Inject
import models._
import services.OwnerLogic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PetLogic @Inject()(repo: PetRepo, ownerLogic: OwnerLogic) {

  import repo.dbConfig.driver.api._

  def getPets(name: Option[String]): Future[List[Pet]] = {
    repo.db.run(
      repo.pets
        .filter(p =>
          name.map { n =>
            p.name.toLowerCase === n.toLowerCase
          }.getOrElse(
            slick.lifted.LiteralColumn(true)
          )
        ).result
    ).map(_.map(_.toPet).toList)
  }


  def getPet(id: Int): Future[Option[Pet]] = {
    repo.db.run(
      repo.pets.filter(_.id === id).result
    ).map(_.map(_.toPet).headOption)
  }

  def updateOrRegisterPet(pet: Pet, id: Int = -1): Future[Unit] = {
    repo.db.run(
      repo.pets.insertOrUpdate(
        PetRecord(
          id,
          pet.name.value,
          pet.dob.value.toString,
          pet.notes
        )
      )
    ).map(_ => ())
  }

  def deletePet(id: Int): Future[Unit] = {
    for {
      _ <- ownerLogic.deleteOwner(id)
      _ <- repo.db.run(repo.pets.filter(_.id === id).delete)
    }
      yield ()
  }
}
