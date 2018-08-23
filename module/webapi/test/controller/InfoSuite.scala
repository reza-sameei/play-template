package controller

import play.api.mvc.{AnyContent, Request}
import play.api.test._
import play.api.test.Helpers._

class InfoSuite extends testkit.Underlay {

    it must "return 404" in tryBy {
        FakeRequest(GET,"/base-url/v1/OPS")
    } { i =>
        i.header.status mustEqual 404
    }

    it must "return 200" in tryBy {
        FakeRequest(GET,"/base-url/v1/")
    } { res =>
        val body = bodyAsString(res)
        info(s"Response: ${res}, Body: ${body}")
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

        info(s"Response: ${response}, Body: ${body}")
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
