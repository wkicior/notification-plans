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


object SaveNotificationPlanService {
  def props(npr: Props): Props = Props(new SaveNotificationPlanService(npr))
  def props(): Props = Props(new SaveNotificationPlanService(NotificationPlanRepository.props()))
}

/**
 * Service for saving the notification plan
 */
class SaveNotificationPlanService(notificationPlanRepositoryProps:Props) extends Actor {
  val log = Logging(context.system, this)
  val notificationPlanRepository = context.actorOf(notificationPlanRepositoryProps) 
  
  def saveNotificationPlan(np:NotificationPlan) = {
    log.info("saveNotificationPlan called: " + np)
    implicit val timeout = Timeout(5000 milliseconds)
    val notificationPlanFuture = notificationPlanRepository ? np
    val savedNotificationPlan = Await.result(notificationPlanFuture, timeout.duration).asInstanceOf[NotificationPlan]
 		sender() ! savedNotificationPlan
  }
  
  def receive = {  
    case np: NotificationPlan => saveNotificationPlan(np)
    case _ => log.error("Unknown message")
  }

}
