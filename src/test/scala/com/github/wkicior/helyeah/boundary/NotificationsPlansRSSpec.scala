package com.github.wkicior.helyeah.boundary

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.json._
import spray.testkit.Specs2RouteTest
import akka.testkit.{EventFilter, ImplicitSender, TestKit, TestProbe}
import spray.http.MediaTypes.{ `application/json` }
import spray.http.HttpCharsets.{ `UTF-8` }
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.actor._
import akka.actor.ActorSystem
import com.github.wkicior.helyeah.service.GetNotificationPlansService
import com.github.wkicior.helyeah.service.GetAllNotificationPlansRequest
import com.github.wkicior.helyeah.model.NotificationPlan
import spray.http.HttpEntity
import spray.http.ContentType
import com.github.wkicior.helyeah.service.GetAllNotificationPlansRequest


class NotificationPlansRSSpec extends Specification with Specs2RouteTest with NotificationPlansServiceRS { 
  def actorRefFactory = system
  
  val getNotificationPlansServiceMockedProps = Props(new Actor {    
    def receive = {      
      case GetAllNotificationPlansRequest => sender() ! Vector(NotificationPlan("test@gmail.com"))
    }
  })
  
  val saveNotificationPlanServiceMockedProps = Props(new Actor {
    def receive = {
      case x:NotificationPlan => sender() ! x      
    }
  })
  
  val jsonNotification = """{
      "email": "test@gmail.com"}"""      
    
  override def getNotificationPlansService = actorRefFactory.actorOf(getNotificationPlansServiceMockedProps)
  override def saveNotificationPlanService = actorRefFactory.actorOf(saveNotificationPlanServiceMockedProps)
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
        responseAs[String].replaceAll("[\n ]+", "") === Vector(NotificationPlan("test@gmail.com")).toJson.toString()
      }
    }
    
    "accept new notification plan on POST /notification-plans path" in {
      Post("/notification-plans", HttpEntity(ContentType(`application/json`, `UTF-8`), jsonNotification)) ~> sealRoute(myRoute) ~> check {
        import com.github.wkicior.helyeah.application.JsonProtocol._
        responseAs[NotificationPlan] mustEqual NotificationPlan("test@gmail.com")
        status should be (OK)
      }
    }
  }
}
