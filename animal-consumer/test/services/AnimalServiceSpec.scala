package services

import play.api.test.PlaySpecification
import model.Alligator
import com.dius.pact.model.{MakePact, Pact}
import com.dius.pact.model.MakeInteraction.given
import com.dius.pact.author.PactServerConfig
import com.dius.pact.consumer.PactVerification
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import com.dius.pact.consumer.ConsumerPact._

/**
 * Test the collection endpoints of the animal service
 */
class AnimalServiceSpec extends PlaySpecification {

  "Animal Service" should {
    val port = 9000
    def url = s"http://localhost:$port"
    def animalService = new AnimalService(url)
    val executionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext

    val alligatorPact = MakePact()
      .withProvider("Animal_Serice")
      .withConsumer("Zoo_App")

    "List All Animals" in {
      def alligatorJson = """[{"name": "Bob"}]"""
      val state = "there are alligators"
      val pact: Pact = alligatorPact.withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for animals",
            method = "GET",
            path = "/animals")
          .willRespondWith(
            200,
            Map("Content-Type" -> "application/json; charset=UTF-8"),
            alligatorJson)
      ).build

      pact.runConsumer(PactServerConfig(port), state) {
        Await.result(animalService.animals(executionContext), Duration(10, "s")) must beEqualTo(Seq(Alligator("Bob")))
      } must beEqualTo(PactVerification.PactVerified).await
    }

    "List All Alligators" in {
      def alligatorJson = """[{"name": "Bob"}]"""
      val state = "there are alligators"
      val pact: Pact = alligatorPact.withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for alligators",
            method = "GET",
            path = "/alligators")
          .willRespondWith(
            200,
            Map("Content-Type" -> "application/json; charset=UTF-8"),
            alligatorJson)
      ).build

      pact.runConsumer(PactServerConfig(port), state) {
        Await.result(animalService.alligators(executionContext), Duration(10, "s")) must beEqualTo(Seq(Alligator("Bob")))
      } must beEqualTo(PactVerification.PactVerified).await
    }

    "Handle Errors" in {
      val state = "an error has occurred"
      val pact: Pact = alligatorPact.withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for alligators",
            method = "GET",
            path = "/alligators")
          .willRespondWith(
            500,
            Map("Content-Type" -> "application/json; charset=UTF-8"),
            """{"error": "Argh!!!"}""")
      )

      pact.runConsumer(PactServerConfig(port), state) {
        Await.result(animalService.alligators(executionContext), Duration(10, "s")) must beEqualTo(Seq())
      } must beEqualTo(PactVerification.PactVerified).await
    }
  }
}
