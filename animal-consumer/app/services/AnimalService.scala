package services

import model.Alligator
import play.api.libs.ws.WS
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.{JsResult, JsValue, Reads}

class AnimalService(url:String) {

  implicit val alligatorReads = new Reads[Alligator] {
    override def reads(j:JsValue):JsResult[Alligator] = {
      (j \ "name").validate[String].map { name => Alligator(name) }
    }
  }

  def alligators(implicit executionContext: ExecutionContext): Future[Seq[Alligator]] = {
    WS.url(s"$url/animals").get().map { r =>
      r.json.as[Seq[Alligator]]
    }
  }
}
