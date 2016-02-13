package com.github.wkicior.helyeah.service

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.actor.Actor
import akka.event.Logging
import akka.actor.Props
import com.github.wkicior.helyeah.model.NotificationPlan
import akka.testkit.ImplicitSender
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.testkit.TestKit
import akka.testkit.TestProbe
import akka.testkit.EventFilter


/**
 * @author disorder
 */
class DeleteNotificaionPlanServiceSpec (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  
    def this() = this(ActorSystem("DeleteNotificationPlanServiceSpec",
      ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))

    override def afterAll {
      TestKit.shutdownActorSystem(system)
    }
    
    val notificationPlansRepositoryProbe = TestProbe()
      val notificationPlansRepositoryProps = Props(new Actor {
        def receive = {          
          case x => notificationPlansRepositoryProbe.ref forward x
        }
    })
    
    val deleteNotificationPlanService = system.actorOf(DeleteNotificationPlanService.props(notificationPlansRepositoryProps))
    
    "A DeleteNotificationPlanService actor" must {
      "reject other message than DeleteNotificationPlanRequest" in {
          EventFilter.error("Unknown message", occurrences = 1) intercept {
            deleteNotificationPlanService ! "fail"
          }
        }
      "accept DeleteNotificationPlanRequest and delete notificaion plan" in {
          val dnpr = DeleteNotificationPlanRequest("id")
          deleteNotificationPlanService ! dnpr
          notificationPlansRepositoryProbe.expectMsg(dnpr)  
      }
    }
}
