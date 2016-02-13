package com.github.wkicior.helyeah.model
import com.novus.salat.annotations._
import org.bson.types.ObjectId

case class NotificationPlan(email:String, id: Option[String] = None)