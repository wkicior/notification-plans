package com.github.wkicior.helyeah.service

import akka.actor.Props
import akka.actor.Actor
import akka.event.Logging
import com.github.wkicior.helyeah.model.NotificationPlan
import com.github.wkicior.helyeah.model.NotificationPlan


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
    case GetAllNotificationPlansRequest =>
      log.info("GetAllNotificationPlansRequest called")
      val notificationPlans: Iterable[NotificationPlan] = dao.getAllNotificaionPlans()
      log.info("found: " + notificationPlans)
      sender() ! notificationPlans
    case _ => log.error("Unknown message")
  }
}
