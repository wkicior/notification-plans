package com.github.wkicior.helyeah.service

import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{TestProbe, EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.Future
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import com.github.wkicior.helyeah.model.NotificationPlan
import akka.util.Timeout


/**
 * @author disorder
 */
class GetNotificationPlansServiceSpec (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {
  
    def this() = this(ActorSystem("GetNotificationPlansServiceSpec",
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

    val getNotificationPlansService = system.actorOf(GetNotificationPlansService.props(notificationPlansRepositoryProps))
    
   "A GetNotificationPlansService actor" must {
    "reject other message than GetAllNotificationPlansRequest" in {
      EventFilter.error("Unknown message", occurrences = 1) intercept {
        getNotificationPlansService ! "fail"
      }
    }
    
    "accept GetAllNotificationPlansRequest and return all notification plans" in { 
        implicit val timeout = Timeout(8000 milliseconds)
        val notificationPlansFuture = getNotificationPlansService ? GetAllNotificationPlansRequest
        notificationPlansRepositoryProbe.expectMsg(GetAllNotificationPlansRequest)
        val notifications:Vector[NotificationPlan] = Vector(NotificationPlan("email@wp.pl"))
        notificationPlansRepositoryProbe.reply(notifications)
        val notificationPlans:Iterable[NotificationPlan] = Await.result(notificationPlansFuture, timeout.duration).asInstanceOf[Iterable[NotificationPlan]]
        notificationPlans should be(notifications)       
    }
  }
}




  



