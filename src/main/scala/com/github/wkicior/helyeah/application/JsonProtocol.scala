package com.github.wkicior.helyeah.application

import com.github.wkicior.helyeah.model.NotificationPlan
import spray.httpx.SprayJsonSupport
import spray.httpx.SprayJsonSupport._
import spray.json._

/**
 * Protocols for model JSON support
 * Created by disorder on 06.03.15.
 */
object JsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
 implicit val notificationPlanFormat = jsonFormat1(NotificationPlan)
}

