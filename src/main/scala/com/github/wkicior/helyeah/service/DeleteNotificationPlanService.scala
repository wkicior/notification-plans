package com.github.wkicior.helyeah.service

import akka.actor.Actor
import akka.event.Logging
import akka.actor.Props
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.Future
import com.github.wkicior.helyeah.model.NotificationPlan

case class DeleteNotificationPlanRequest(id:String)
case class DeleteNotificationPlanResponse()


object DeleteNotificationPlanService {
  def props(npr: Props): Props = Props(new DeleteNotificationPlanService(npr))
  def props(): Props = Props(new DeleteNotificationPlanService(NotificationPlanRepository.props()))
}

/**
 * Service for deleting the notification plan
 */
class DeleteNotificationPlanService(notificationPlanRepositoryProps:Props) extends Actor {
  val log = Logging(context.system, this)
  val notificationPlanRepository = context.actorOf(notificationPlanRepositoryProps) 
  
  def deleteNotificationPlan(delRequest:DeleteNotificationPlanRequest) = {
    log.info("deleteNotificationPlan called: " + delRequest)
    notificationPlanRepository ! delRequest
  }    
  
  def receive = {
    case req: DeleteNotificationPlanRequest => deleteNotificationPlan(req)
    case _ => log.error("Unknown message")
  }

}