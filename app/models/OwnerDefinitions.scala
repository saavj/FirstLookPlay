package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class OwnerName(firstName: String, lastName: String)

object OwnerName {
  implicit val ownerNameFormat = Json.format[OwnerName]
}

case class Address(firstLine: String, town: String, postcode: String)

object Address {
  implicit val addressFormat = Json.format[Address]
}

case class PhoneNumber(value: String)

object PhoneNumber

case class Owner(name: OwnerName, address: Address, phoneNumber: PhoneNumber)

object Owner {
  val ownerReads: Reads[Owner] = (
    (JsPath \ "ownerName").read[OwnerName] and
      (JsPath \ "address").read[Address] and
      (JsPath \ "phoneNumber").read[String].map { value => PhoneNumber(value) }
    ) (Owner.apply _)

  val ownerWrites: Writes[Owner] = (
    (JsPath \ "ownerName").write[OwnerName] and
      (JsPath \ "address").write[Address] and
      (JsPath \ "phoneNumber").write[String].contramap { (number: PhoneNumber) => number.value }
    ) (unlift(Owner.unapply))

  implicit val ownerFormat: Format[Owner] = Format(ownerReads, ownerWrites)
}