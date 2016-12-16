package models

import org.scalatest.Matchers._
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}

class OwnerDefinitionsSpec extends PlaySpec {

  "Owner Formats" should {

    val json: JsValue = Json.parse(
      s"""
         |{
         |  "ownerName": {
         |    "firstName": "Sofia",
         |    "lastName": "Cole"
         |   },
         |  "address": {
         |    "firstLine": "1 Privet Drive",
         |    "town": "Surrey",
         |    "postcode": "HP1 P77"
         |   },
         |   "phoneNumber": "07834823846"
         |}
         |
         """.stripMargin)

    val owner = Owner(
      OwnerName("Sofia", "Cole"),
      Address(
        "1 Privet Drive",
        "Surrey",
        "HP1 P77"
      ),
      PhoneNumber("07834823846")
    )

    "write to Json" in {
      Json.toJson(owner) shouldBe json
    }

    "get Owner from Json" in {
      json.as[Owner] shouldBe owner
    }
  }
}

