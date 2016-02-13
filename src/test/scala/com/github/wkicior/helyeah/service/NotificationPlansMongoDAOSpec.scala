package com.github.wkicior.helyeah.service

import org.specs2._
import org.specs2.mutable.Before
import com.github.wkicior.helyeah.model.NotificationPlan
import com.mongodb.casbah.MongoClient

/**
 * @author disorder
 */
class NotificationPlansMongoDAOSpec extends Specification with Before {
  object NotificationPlansMongoDAOTest extends NotificationPlansMongoDAO {
     val mongoClient =  MongoClient("notification-plans-mongo", 27017)
     val db = mongoClient("notification-plans-test-db")
     val collection = db("notification-plans")
  }
  
  val plan1: NotificationPlan = NotificationPlan("test@localhost")
  
  def is = sequential ^ s2"""

 This is a specification to check the NotificationPlansRepository

 The NotificationPlansRepository should
   return an empty collection                                    $e1
   save new notification plan                                    $create
   return all notification plans                                 $getAll
   delete saved notification plan                                $delete
                                                                 """
  val notificationsMongoDAO = NotificationPlansMongoDAOTest.collection
  
  
  def e1 = NotificationPlansMongoDAOTest.count() mustEqual 0
  
  def create = {
    NotificationPlansMongoDAOTest.save(plan1)
    NotificationPlansMongoDAOTest.save(NotificationPlan("test2@localhost")) 
    notificationsMongoDAO.find().count() mustEqual 2
  }
  
  def getAll = {
    val plans:Iterable[NotificationPlan] = NotificationPlansMongoDAOTest.getAllNotificaionPlans()
    plans.size mustEqual 2
  }
  
  def delete = {
    val plans:Iterable[NotificationPlan] = NotificationPlansMongoDAOTest.getAllNotificaionPlans()
    println(plans)
    NotificationPlansMongoDAOTest.delete(plans.head.id.get)
    notificationsMongoDAO.find().count() mustEqual 1
  }
  
  def before = NotificationPlansMongoDAOTest.db.dropDatabase()
}
  
