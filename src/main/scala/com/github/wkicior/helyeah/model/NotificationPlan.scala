package com.github.wkicior.helyeah.model
import com.novus.salat.annotations._

case class NotificationPlan(email:String, @Key("_id") id: Option[String] = None)