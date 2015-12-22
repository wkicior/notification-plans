package com.github.wkicior.helyeah.service

import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{TestProbe, EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.Future
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import com.github.wkicior.helyeah.model.NotificationPlan
import akka.util.Timeout

/**
 * @author disorder
 */
class SaveNotificationPlanServiceSpec (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  
    def this() = this(ActorSystem("SaveNotificationPlanServiceSpec",
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
    
    val saveNotificationPlanService = system.actorOf(SaveNotificationPlanService.props(notificationPlansRepositoryProps))
    
    "A SaveNotificationPlanService actor" must {
    "reject other message than NotificationPlan" in {
        EventFilter.error("Unknown message", occurrences = 1) intercept {
          saveNotificationPlanService ! "fail"
        }
      }
    }
    
    "accept NotificationPlan, save it and return it to the caller" in { 
        implicit val timeout = Timeout(8000 milliseconds)
        val notificationPlan = NotificationPlan("email@helyean.com")
        val notificationPlansFuture = saveNotificationPlanService ? notificationPlan
        notificationPlansRepositoryProbe.expectMsg(notificationPlan)        
        notificationPlansRepositoryProbe.reply(notificationPlan)
        val savedNotificationPlan:NotificationPlan = Await.result(notificationPlansFuture, timeout.duration).asInstanceOf[NotificationPlan]
        savedNotificationPlan should be(notificationPlan)       
    }
}