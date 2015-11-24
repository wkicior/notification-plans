package com.github.wkicior.helyeah.service

import akka.actor.Props
import akka.actor.Actor
import akka.event.Logging


object NotificationPlanRepository {
  def props(dao:NotificationPlansMongoDAO): Props = Props(new NotificationPlanRepository(dao))
  def props(): Props = Props(new NotificationPlanRepository(NotificationPlansMongoDAO))

}
/**
 * @author disorder
 */
class NotificationPlanRepository(dao:NotificationPlansMongoDAO) extends Actor {
  val log = Logging(context.system, this)
  val collection = NotificationPlansMongoDAO.collection
  def receive = {
    //case q:QueryLastNotificationMessage =>
      //log.info("query: " + q.notificationPlan)
      //val notification:Option[Notification]= dao.findLastByPlan(q.notificationPlan)
      //log.info("found: " + notification)
      //sender() ! notification
    case _ => log.error("Unknown message")
  }
}
