package controllers

import play.api.mvc._
import play.api.libs.json.{JsValue, Json}

object Application extends Controller {

  private var state: String = "default"

  private val fixtures = Map(
    "default" -> InternalServerError("state not set"),
    "there are alligators" -> Ok(Json.arr(Json.obj("name" -> "Bob"))),
    "an error has occurred" -> InternalServerError(Json.obj("error" -> "Argh!!!")),
    "there is an alligator named Mary" -> Ok(Json.obj("name" -> "Mary")),
    "there is not an alligator named Mary" -> NotFound
  )

  def index = Action {
    fixtures(state)
  }

  def find(name: String) = Action {
    fixtures(state)
  }

  def enterState = Action(parse.json) { request =>
    state = (request.body \ "state").as[String]
    Ok
  }

}