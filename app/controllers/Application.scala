package controllers

import com.google.inject.Inject
import models.{Owner, Pet}
import play.api.libs.json.Json
import play.api.mvc._
import services.OwnerLogic
import services.database.PetLogic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(petLogic: PetLogic, ownerLogic: OwnerLogic) extends Controller {
  val requestErrorMessage = "See Documentation about correct request payload"

  def getPets(name: Option[String]): Action[AnyContent] = Action.async {
    petLogic.getPets(name).map(r =>
      Ok(Json.toJson(r))
    )
  }

  def getPetById(id: Int): Action[AnyContent] = Action.async {
    petLogic.getPet(id).map(r =>
      r.fold(NotFound("Pet with that ID does not exist")) { _ =>
        Ok(Json.toJson(r))
      }
    )
  }

  def updateOwnerByPetId(petId: Int): Action[AnyContent] = Action.async { request =>
    request.body.asJson.fold(Future.successful(BadRequest(requestErrorMessage))) { payload =>
      ownerLogic.updateOwner(petId, payload.as[Owner]).map(_ =>
        Ok("Success!")
      )
    }
  }

  def updatePetById(id: Int): Action[AnyContent] = Action.async { request =>
    request.body.asJson.fold(Future.successful(BadRequest(requestErrorMessage))) { payload =>
      petLogic.updateOrRegisterPet(payload.as[Pet], id).map(_ =>
        Ok("Success!")
      )
    }
  }

  def registerPet: Action[AnyContent] = Action.async { request =>
    request.body.asJson.fold(Future.successful(BadRequest(requestErrorMessage))) { payload =>
      petLogic.updateOrRegisterPet(payload.as[Pet]).map(_ =>
        Ok("Success!")
      )
    }
  }

  def deletePet(id: Int): Action[AnyContent] = Action.async {
    petLogic.deletePet(id).map(_ =>
      Ok("Success!")
    )
  }

  def addOwnerByPetId(petId: Int) = Action.async { request =>
    request.body.asJson.fold(Future.successful(BadRequest(requestErrorMessage))) { payload =>
      ownerLogic.addOwner(petId, payload.as[Owner]).map(_ =>
        Ok("Success!")
      )
    }
  }
}

class BadIncomingRequest extends Exception