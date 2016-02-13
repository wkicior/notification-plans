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
import com.github.wkicior.helyeah.service.SaveNotificationPlanService
import akka.util.Timeout
import com.github.wkicior.helyeah.service.DeleteNotificationPlanService
import com.github.wkicior.helyeah.service.DeleteNotificationPlanRequest


class ForecastNotificationsHistoryRSActor extends Actor with NotificationPlansServiceRS {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
  
}

trait NotificationPlansServiceRS extends HttpService {
  import com.github.wkicior.helyeah.application.JsonProtocol._
  def getNotificationPlansService = actorRefFactory.actorOf(GetNotificationPlansService.props)
  def saveNotificationPlanService = actorRefFactory.actorOf(SaveNotificationPlanService.props) 
  def deleteNotificationPlanService = actorRefFactory.actorOf(DeleteNotificationPlanService.props)
  
  val myRoute =
    pathPrefix("notification-plan" / Rest) {
        id => delete {
          complete {
            deleteNotificationPlanService ! DeleteNotificationPlanRequest(id)
            id
          }
      }
    } ~
    pathPrefix("notification-plans") {
      get {
          complete {
        	  implicit val timeout = Timeout(8000 milliseconds)
            val notificationPlansFuture = getNotificationPlansService ? GetAllNotificationPlansRequest            
            val notificationPlans:Iterable[NotificationPlan] = Await.result(notificationPlansFuture, timeout.duration).asInstanceOf[Iterable[NotificationPlan]]
            notificationPlans
          }
      } ~
      post {
           entity(as[NotificationPlan]) { postedNotification =>
              complete {
                implicit val timeout = Timeout(8000 milliseconds)
                val notificationPlanFuture = saveNotificationPlanService ? postedNotification
                val savedNotification: NotificationPlan = Await.result(notificationPlanFuture, timeout.duration).asInstanceOf[NotificationPlan]
                savedNotification
              }
           }           
      }
    } 
}