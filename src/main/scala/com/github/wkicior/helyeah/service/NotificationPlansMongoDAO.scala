package com.github.wkicior.helyeah.service
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers

/**
 * @author disorder
 */
abstract class NotificationPlansMongoDAO {
    val collection:MongoCollection
    val mongoClient:MongoClient
    
    def close() {
      mongoClient.close()
    }
}
object NotificationPlansMongoDAO extends NotificationPlansMongoDAO {
  RegisterConversionHelpers()
  RegisterJodaTimeConversionHelpers()
  val mongoClient = MongoClient("notification-plans-mongo", 27017)
  val db = mongoClient("notification-plans-db")
  val collection = db("notification-plans")  
}
