package com.giantcroissant

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

import spray.http._
import spray.httpx.PlayJsonSupport._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.routing._

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(
  name: String = "",
  email: String = ""
)

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("my-system")

  implicit val userReads: Reads[User] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "email").read[String]
  )(User.apply _)
  
  //implicit val userReads = Json.reads[User]

  def unmarshaller: Unmarshaller[User] = playJsonUnmarshaller[User]

  startServer(interface = "localhost", port = 8080) {
    path("users") {
      get {
        complete {
          """{ "results": [ { "name", "Ray" } ] }"""
        }
      } ~
      post {
        entity(as[User]) { user =>
          complete {
            s"""{
              id: 9d0fkd,
              name: ${user.name},
              email: ${user.email}
            }"""
          }
        }
      }
    }
  }
}
