package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  /**
   * In a real application you would use the configuration to source your persistence mechanism and endpoint urls on startup
   * @return
   */
  private def state: String = play.api.Play.current.configuration.getString("state").getOrElse("default")

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

}