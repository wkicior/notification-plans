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
import akka.util.Timeout
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest

/**
 * @author disorder
 */
class NotificationPlanRepositorySpec (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll with MockFactory with OneInstancePerTest {
  def this() = this(ActorSystem("NotificationPlanRepositorySpec",
    ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
    
  val mockNotificationPlansMongoDAO = mock[NotificationPlansMongoDAO]
  val notificationRepositoryMocked = system.actorOf(NotificationPlanRepository.props(mockNotificationPlansMongoDAO))
  

  "An NotificationRepository actor" must {
    "reject other message than GetAllNotificationPlansRequest" in {
      EventFilter.error("Unknown message", occurrences = 1) intercept {
        notificationRepositoryMocked ! "fail"
      }
    }
  }
  "accept GetAllNotificationPlansRequest and return all notification plans" in {
    val notificationPlans:Iterable[NotificationPlan] = Vector(NotificationPlan("aaa"))
    (mockNotificationPlansMongoDAO.getAllNotificaionPlans _).expects().returning(notificationPlans)
    notificationRepositoryMocked ! GetAllNotificationPlansRequest
    expectMsg(8 seconds, notificationPlans)
  }
  
  "accept NotificationPlan, save it and return" in  {
    val npNew = NotificationPlan("aaa")
    val npNewAfterSave = NotificationPlan("aaa", Option[String]("1"))
    (mockNotificationPlansMongoDAO.save _).expects(npNew).returning(npNewAfterSave)
    notificationRepositoryMocked ! npNew
    val notificationPlan:NotificationPlan = expectMsg(8 seconds, npNewAfterSave)
  }
  
  "accept DeleteNotificationPlansRequest and delete previously saved notification plan" in {
	    (mockNotificationPlansMongoDAO.delete _).expects("aaa")
      val res = notificationRepositoryMocked ! DeleteNotificationPlanRequest("aaa")
      expectMsg(DeleteNotificationPlanResponse)    
  } 
}