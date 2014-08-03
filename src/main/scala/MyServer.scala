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

import reactivemongo.api._

import play.modules.reactivemongo.json.collection.JSONCollection

case class User(
  name: String = "",
  email: String = ""
)

object Main extends App with SimpleRoutingApp {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val system = ActorSystem("my-system")

  implicit val userReads: Reads[User] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "email").read[String]
  )(User.apply _)
  
  //implicit val userReads = Json.reads[User]

  def unmarshaller: Unmarshaller[User] = playJsonUnmarshaller[User]

  def connectToDB() = {
    import reactivemongo.api._
    //import scala.concurrent.ExecutionContext.Implicits.global

    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))

    val db = connection("mq2")

    db
  }

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
            //user
            s"""{
              id: 9d0fkd,
              name: ${user.name},
              email: ${user.email}
            }"""
          }
        }
      }
    } ~
    path("projects") {
      get {
        complete {
          """{ "results": [ { "name", "Small World" } ] }"""
        }
      } ~
      post {
        entity(as[JsObject]) { project =>
          complete {
            val db = connectToDB()
            def collection: JSONCollection = db.collection[JSONCollection]("projects")

            collection.insert(project)
            project
          }
        }
      }
    }
  }
}
