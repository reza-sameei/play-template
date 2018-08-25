package controller

import play.api.mvc.{AnyContent, Request}
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

class InfoSuite extends testkit.Underlay {

    it must "return 404" in tryBy {
        FakeRequest(GET,"/base-url/v1/OPS")
    } { i =>
        i.header.status mustEqual 404
    }

    it must "return 200" in tryBy {
        FakeRequest(GET,"/service/api/v1/")
    } { res =>
        val body = bodyAsString(res)
        // info(s"Response: ${res}, Body: ${body}")
        res.header.status mustEqual 200
    }

    it must "?" in {

        val req = FakeRequest(POST, "/OPS")
            .withBody("{'key': 'vlaue'}")
            .asInstanceOf[Request[AnyContent]]

        val response = await {
            underlay.controllerInstance.info.summary().apply(req)
        }

        val body = bodyAsString(response)

        // info(s"Response: ${response}, Body: ${body}")
    }


    it must "???" in tryBy {
        FakeRequest(GET, "/service/api/v1/person/")
    } { res =>
        import format.PlayJSON._
        val body = bodyAsJSON[Seq[model.hard.Person]](res)
        info(s"Response: \n\t\t${res}, \n\t\tBody: ${body}")
        res.header.status mustEqual 200

        body.size mustBe 2
        body.head.internal mustBe model.hard.PersonID(1)
    }

    it must "Try to add, but get Error(400): Duplicated PersonID " in tryBy {
        import format.PlayJSON._
        import model.hard._
        val person = Person(PersonID(1), NickName("RezaT"), Email("reza.samee@gmail.com"))

        FakeRequest(POST, "/service/api/v1/person/").withHeaders(
            "Content-Type" -> "text/json"
        ).withBody(Json.toJson(person))
    } { res =>
        val body = bodyAsString(res)
        info(s"Response: ${res}, ${body}")
        info(s"Error: ${res.header.headers("X-ERR")}")
        res.header.status mustBe 400
    }

    it must "Try to add, but get Error(400): Duplicated Email " in tryBy {
        import format.PlayJSON._
        import model.hard._
        val person = Person(PersonID(3), NickName("RezaT"), Email("reza.samee@gmail.com"))

        FakeRequest(POST, "/service/api/v1/person/").withHeaders(
            "Content-Type" -> "text/json"
        ).withBody(Json.toJson(person))
    } { res =>
        val body = bodyAsString(res)
        info(s"Response: ${res}, ${body}")
        info(s"Error: ${res.header.headers("X-ERR")}")
        res.header.status mustBe 400
    }

    it must "Try to add and do it!" in tryBy {
        import format.PlayJSON._
        import model.hard._
        val person = Person(PersonID(3), NickName("RezaT"), Email("reza.t@gmail.com"))

        FakeRequest(POST, "/service/api/v1/person/").withHeaders(
            "Content-Type" -> "text/json"
        ).withBody(Json.toJson(person))
    } { res =>
        val body = bodyAsString(res)
        info(s"Response: ${res}, ${body}")
        res.header.status mustBe 201
    }

    it must "Try to get new list with 3 items" in tryBy {
        FakeRequest(GET, "/service/api/v1/person/")
    } { res =>
        import format.PlayJSON._
        val body = bodyAsJSON[Seq[model.hard.Person]](res)
        info(s"Response: \n\t\t${res}, \n\t\tBody: ${body}")
        res.header.status mustEqual 200

        body.size mustBe 3
        body.head.internal mustBe model.hard.PersonID(1)
    }



    /*it must "return 404" in {
        tryBy { FakeRequest(GET,"/joon-sik/OPS") } { i =>
            i.header.status mustEqual 404
        }
    }

    it must "return 400" in {
        tryBy {
            FakeRequest(
                POST, "/alpha/joon-sik/v1/983498-dsjhj3-3242hjsd"
            ).withBody("{'key':'val'}")
        } { i =>
            info(i.toString)
            i.header.status mustEqual 400
        }
    }

    it must "Store single" in {
        tryBy {
            FakeRequest(
                PUT, "/alpha/joon-sik/v1/983498-dsjhj3-3242hjsd/123456"
            ).withBody("{'key':'val'}")
        } { rsp =>
            info(rsp.toString)
            rsp.header.status mustEqual 200
        }
    }

    it must "Store bulk" in {
        tryBy {
            FakeRequest(
                PUT, "/alpha/joon-sik/v1/983498-dsjhj3-3242hjsd"
            ).withBody("{'key':'val'}")
        } { rsp =>
            info(rsp.toString)
            rsp.header.status mustEqual 200
        }
    }*/



}
