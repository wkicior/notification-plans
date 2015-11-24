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

object GetAllNotificationPlansRequest

object GetNotificationPlansService {
  def props(nprProps:Props): Props = Props(new GetNotificationPlansService(nprProps))
  def props(): Props = Props(new GetNotificationPlansService(NotificationPlanRepository.props()))
}

/**
 * Main service for getting the notification plans
 */
class GetNotificationPlansService(notificationPlanRepositoryProps:Props) extends Actor {
  val log = Logging(context.system, this)
  val notificationPlanRepository = context.actorOf(notificationPlanRepositoryProps) 
  
  def getAllNotifications() = {
    log.info("getAllNotifications called")
    implicit val timeout = Timeout(5000 milliseconds)
    val notificationPlansFuture = notificationPlanRepository ? GetAllNotificationPlansRequest
    val notificationPlans = Await.result(notificationPlansFuture, timeout.duration).asInstanceOf[Iterable[NotificationPlan]]
 		sender() ! notificationPlans
    
  }

  def receive = {
    case GetAllNotificationPlansRequest => getAllNotifications() 
    case _ => log.error("Unknown message")
  }

}
