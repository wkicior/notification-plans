package com.github.wkicior.helyeah.boundary

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.json._
import spray.testkit.Specs2RouteTest
import akka.testkit.{EventFilter, ImplicitSender, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.actor._
import akka.actor.ActorSystem
import com.github.wkicior.helyeah.service.GetNotificationPlansService
import com.github.wkicior.helyeah.service.GetAllNotificationPlansRequest
import com.github.wkicior.helyeah.model.NotificationPlan


class NotificationPlansRSSpec extends Specification with Specs2RouteTest with NotificationPlansServiceRS { 
  def actorRefFactory = system
  
  val getNotificationPlansServiceMockedProps = Props(new Actor {    
    def receive = {      
      case x => sender() ! Vector(NotificationPlan("test@gmail.com"))
    }
  })
    
  override def getNotificationPlansService = actorRefFactory.actorOf(getNotificationPlansServiceMockedProps)
  "NotificationPlansServiceRS" should {   
    
    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return all notification plans on GET /notification-plans path" in {
      Get("/notification-plans") ~> sealRoute(myRoute) ~> check {
        status should be(OK)        
        import com.github.wkicior.helyeah.application.JsonProtocol._
        println(responseAs[String].replaceAll("[\n ]+", ""))
        //replaceAll("""(?m)\s+$""", "")
        responseAs[String].replaceAll("[\n ]+", "") === Vector(NotificationPlan("test@gmail.com")).toJson.toString()
      }
    }
  }
}
