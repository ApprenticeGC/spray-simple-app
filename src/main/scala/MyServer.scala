package com.giantcroissant

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

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
        complete {
          """{ "results": [ { "id": "rer903", "name", "Ray" } ] }"""
        }
      }
    }
  }
}
