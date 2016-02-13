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
import org.bson.types.ObjectId

/**
 * MongoDB representation of model NotificationPlan - just to handle _id as ObjectId
 */
private case class NotificationPlanMongo(email:String, _id:ObjectId)
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
      val notificationPlansMongo = collection.map(grater[NotificationPlanMongo].asObject(_))
      notificationPlansMongo.map(mapNotificationPlan(_))
    }
    
    def save(notificationPlan:NotificationPlan):NotificationPlan = {
      val notificationPlanObj = grater[NotificationPlan].asDBObject(notificationPlan)
      collection += notificationPlanObj
      return mapNotificationPlan(grater[NotificationPlanMongo].asObject(notificationPlanObj))    
    }
    
    def delete(id:String) = {
      collection.remove("_id" $eq new ObjectId(id))
    }
    
    def mapNotificationPlan(npm:NotificationPlanMongo): NotificationPlan = {
        NotificationPlan(npm.email, Option(npm._id.toString()))
    }
}
object NotificationPlansMongoDAO extends NotificationPlansMongoDAO {
  RegisterConversionHelpers()
  RegisterJodaTimeConversionHelpers()
  val mongoClient = MongoClient("notification-plans-mongo", 27017)
  val db = mongoClient("notification-plans-db")
  val collection = db("notification-plans")  
}
