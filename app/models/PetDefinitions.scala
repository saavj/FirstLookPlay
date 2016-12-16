package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, Json, _}

case class Name(value: String)

object Name {
  implicit val nameFormat = Json.format[Name]
}

case class DOB(value: String)

object DOB {
  implicit val dobFormat = Json.format[DOB]
}

case class Pet(id: Option[Int], name: Name, dob: DOB, notes: String)

object Pet {

  val petReads: Reads[Pet] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String].map { value => Name(value) } and
      (JsPath \ "dateOfBirth").read[String].map { value => DOB(value) } and
      (JsPath \ "notes").read[String]
    ) (Pet.apply _)


  val petWrites: Writes[Pet] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "name").write[String].contramap { (name: Name) => name.value } and
      (JsPath \ "dateOfBirth").write[String].contramap { (dob: DOB) => dob.value } and
      (JsPath \ "notes").write[String]
    ) (unlift(Pet.unapply))

  implicit val petFormat: Format[Pet] =
    Format(petReads, petWrites)
}