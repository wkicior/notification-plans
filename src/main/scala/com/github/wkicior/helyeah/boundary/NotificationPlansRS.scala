package com.github.wkicior.helyeah.boundary

import akka.actor.Actor
import spray.routing._

class ForecastNotificationsHistoryRSActor extends Actor with NotificationPlansServiceRS {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}


trait NotificationPlansServiceRS extends HttpService {
  import com.github.wkicior.helyeah.application.JsonProtocol._
  val myRoute =
    pathPrefix("ads") {
      get {
          complete {
            //TO getter
            case class Contact(email: String)
            case class User(id: Int, name: String, contact: Contact)
            case class Ad(id:Int, title: String, desc: String, owner:User)
            val ads = Ad(id=1, title="loremxxxxyyy2",desc="ipsum", owner=User(id=1, name="Dolor", contact = Contact(email="lorem@ipsum.pl")))::
            Ad(id=2, title="lorem",desc="ipsum", owner=User(id=1, name="Dolor", contact = Contact(email="lorem@ipsum.pl")))::
            Ad(id=3, title="lorem",desc="ipsum", owner=User(id=1, name="Dolor", contact = Contact(email="lorem@ipsum.pl")))::
            Nil
            ads.toString
          }
      }     
    }
}