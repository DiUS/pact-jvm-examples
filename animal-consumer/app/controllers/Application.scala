package controllers

import play.api._
import play.api.mvc._
import services.AnimalService

object Application extends Controller {
  lazy val alligatorService = new AnimalService(Play.current.configuration.getString("alligator.service").get)

  def index = Action.async {
    implicit val executionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
    alligatorService.alligators.map { alligators =>
      Ok(views.html.index(s"Your new application is ready. There are ${alligators.size} alligators"))
    }
  }
}