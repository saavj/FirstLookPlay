package models

import org.scalatest.Matchers._
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}

class PetDefinitionsSpec extends PlaySpec {

  "Pet Formats" should {

    val json: JsValue = Json.parse(
      s"""
         |{
         |"name": "Dutchie",
         |"dateOfBirth": "2016-01-01",
         |"notes": "Healthy"
         |}
         |
         """.stripMargin)

    val pet = Pet(
      id = None,
      name = Name("Dutchie"),
      dob = DOB("2016-01-01"),
      notes = "Healthy"
    )

    "write to Json" in {
      Json.toJson(pet) shouldBe json
    }

    "get Pet from Json" in {
      json.as[Pet] shouldBe pet
    }
  }
}