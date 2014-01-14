package services

import play.api.test.PlaySpecification
import model.Alligator
import com.dius.pact.model.{MakePact, Pact}
import com.dius.pact.model.MakeInteraction.given
import com.dius.pact.author.PactServerConfig
import com.dius.pact.consumer.PactVerification
import scala.concurrent.duration.Duration
import scala.concurrent.Await


class AnimalServiceSpec extends PlaySpecification {
  def alligatorJson = """[{"name": "Bob"}]"""

  "Animal Service" should {
    val state = "there are alligators"

    val pact: Pact = MakePact()
      .withProvider("Alligator_Serice")
      .withConsumer("Zoo_App")
      .withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for animals",
            method = "GET",
            path = "/animals")
          .willRespondWith(
            200,
            Map("Content-Type" -> "application/json; charset=UTF-8"),
            alligatorJson
          )
        ).build

    "List All Alligators" in {
      val port = 9000
      def url = s"http://localhost:$port"
      def animalService = new AnimalService(url)

      import com.dius.pact.consumer.ConsumerPact._
      val executionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext

      pact.runConsumer(PactServerConfig(port), state) {
        Await.result(animalService.alligators(executionContext), Duration(10, "s")) must beEqualTo(Seq(Alligator("Bob")))
      } must beEqualTo(PactVerification.PactVerified).await
    }
  }
}
