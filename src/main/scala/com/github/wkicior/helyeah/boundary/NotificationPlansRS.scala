package com.github.wkicior.helyeah.boundary

import akka.actor.Actor
import spray.routing._
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.Future
import com.github.wkicior.helyeah.model.NotificationPlan
import spray.http._
import spray.json._
import spray.client.pipelining._
import com.github.wkicior.helyeah.service.GetNotificationPlansService
import com.github.wkicior.helyeah.service.GetAllNotificationPlansRequest
import akka.util.Timeout


class ForecastNotificationsHistoryRSActor extends Actor with NotificationPlansServiceRS {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
  
}

trait NotificationPlansServiceRS extends HttpService {
  import com.github.wkicior.helyeah.application.JsonProtocol._
  def getNotificationPlansService = actorRefFactory.actorOf(GetNotificationPlansService.props)
  
  val myRoute =
    pathPrefix("notification-plans") {
      get {
          complete {
        	  implicit val timeout = Timeout(8000 milliseconds)
            val notificationPlansFuture = getNotificationPlansService ? GetAllNotificationPlansRequest            
            val notificationPlans:Iterable[NotificationPlan] = Await.result(notificationPlansFuture, timeout.duration).asInstanceOf[Iterable[NotificationPlan]]
            notificationPlans
          }
      }     
    }
}