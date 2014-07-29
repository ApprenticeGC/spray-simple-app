package com.giantcroissant

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

import spray.http._
import spray.httpx.unmarshalling._
import spray.httpx.SprayJsonSupport._
import spray.routing._

import play.api.libs.json._

import spray.json._

import DefaultJsonProtocol._

case class User(name: String = "")

object User extends DefaultJsonProtocol {// with SprayJsonSupport {
  implicit val format = jsonFormat1(apply)
}

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("my-system")

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
            """{ "id": 9d0fkd, "name": """ + user.name + """ }"""
          }
        }
      }
    }
  }
}
