package com.github.wkicior.helyeah.service

import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{TestProbe, EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import com.github.wkicior.helyeah.model._
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import com.mongodb.casbah.MongoClient
import akka.util.Timeout

/**
 * @author disorder
 */
class NotificationPlanRepositorySpec (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("NotificationPlanRepositorySpec",
    ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  
  object NotificationPlansMongoDAOTest extends NotificationPlansMongoDAO {
     val mongoClient =  MongoClient("notification-plans-mongo", 27017)
     val db = mongoClient("notification-plans-test-db-2")
     val collection = db("notification-plans")
     
     override def getAllNotificaionPlans():Iterable[NotificationPlan] = {
      return Vector(NotificationPlan("aaa"))
    }
  }

  val notificationRepository = system.actorOf(NotificationPlanRepository.props(NotificationPlansMongoDAOTest))

  "An NotificationRepository actor" must {
    "reject other message than GetAllNotificationPlansRequest" in {
      EventFilter.error("Unknown message", occurrences = 1) intercept {
        notificationRepository ! "fail"
      }
    }
  }
  "accept GetAllNotificationPlansRequest and return all notification plans" in {
    implicit val timeout = Timeout(8000 milliseconds)
    val notificationPlansFuture = notificationRepository ? GetAllNotificationPlansRequest
    val notificationPlans = Await.result(notificationPlansFuture, timeout.duration).asInstanceOf[Iterable[NotificationPlan]];
    notificationPlans.size should be > 0
  }
  "accept NotificationPlan, save it and return"in  {
    implicit val timeout = Timeout(8000 milliseconds)
    val npNew = NotificationPlan("aaa")
    val notificationPlanFuture = notificationRepository ? npNew
    val notificationPlan = Await.result(notificationPlanFuture, timeout.duration).asInstanceOf[NotificationPlan];
    notificationPlan should be(npNew)
  }
  
}