package com.github.wkicior.helyeah.service

import akka.actor.Actor
import akka.event.Logging
import akka.actor.Props
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.Future


object SaveNotificationPlanService { 
  def props(): Props = Props(new SaveNotificationPlanService())
}

/**
 * Service for saving the notification plan
 */
class SaveNotificationPlanService() extends Actor {
  val log = Logging(context.system, this)
    def receive = {    
    case _ => log.error("Unknown message")
  }

}
