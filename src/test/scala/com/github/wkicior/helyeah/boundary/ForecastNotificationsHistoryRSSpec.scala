package com.github.wkicior.helyeah.boundary

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.json._
import spray.testkit.Specs2RouteTest


class ForecastNotificationsHistoryRSSpec extends Specification with Specs2RouteTest with ForecastNotificationServiceRS {
  def actorRefFactory = system
  
  "MyService" should {

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for GET requests to the /notifications path" in {
      Get("/notifications") ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: POST"
      }
    }
  }
}
