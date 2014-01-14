package services

import model.Alligator
import play.api.libs.ws.WS
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.{JsResult, JsValue, Reads}
import scala.util.control.NonFatal

class AnimalService(url:String) {

  implicit val alligatorReads = new Reads[Alligator] {
    override def reads(j:JsValue):JsResult[Alligator] = {
      (j \ "name").validate[String].map { name => Alligator(name) }
    }
  }

  private def get(path: String)(implicit executionContext: ExecutionContext): Future[Seq[Alligator]] = {
    WS.url(s"$url/$path").get().map { r =>
      if(r.status == 200) {
        r.json.as[Seq[Alligator]]
      } else {
        Seq()
      }
    }
  }

  def alligators(implicit executionContext: ExecutionContext): Future[Seq[Alligator]] = {
    get("alligators")
  }

  def animals(implicit executionContext: ExecutionContext): Future[Seq[Alligator]] = {
    get("animals")
  }

  def findAlligator(name: String)(implicit executionContext: ExecutionContext): Future[Option[Alligator]] = {
    WS.url(s"$url/alligators/$name").get().map { r =>
      if(r.status == 200) {
        Some(r.json.as[Alligator])
      } else {
        None
      }
    }
  }
}
