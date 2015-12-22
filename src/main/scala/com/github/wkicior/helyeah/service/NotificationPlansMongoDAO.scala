package com.github.wkicior.helyeah.service
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.github.wkicior.helyeah.model.NotificationPlan

/**
 * @author disorder
 */
abstract class NotificationPlansMongoDAO {
    val collection:MongoCollection
    val mongoClient:MongoClient
    
    def count():Int = {
      collection.count()
    }
    
    def close() {
      mongoClient.close()
    }

    def getAllNotificaionPlans():Iterable[NotificationPlan] = {
      collection.map(grater[NotificationPlan].asObject(_))
    }
    
    def save(notificationPlan:NotificationPlan):NotificationPlan = {
      val notificationPlanObj = grater[NotificationPlan].asDBObject(notificationPlan)
      collection += notificationPlanObj
      return grater[NotificationPlan].asObject(notificationPlanObj)    
    }
}
object NotificationPlansMongoDAO extends NotificationPlansMongoDAO {
  RegisterConversionHelpers()
  RegisterJodaTimeConversionHelpers()
  val mongoClient = MongoClient("notification-plans-mongo", 27017)
  val db = mongoClient("notification-plans-db")
  val collection = db("notification-plans")  
}
