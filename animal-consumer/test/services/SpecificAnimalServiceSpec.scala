package services

import org.specs2.mutable.Specification
import com.dius.pact.model.{Pact, MakeInteraction, MakePact}
import MakeInteraction.given
import com.dius.pact.consumer.ConsumerPact._
import com.dius.pact.author.PactServerConfig
import scala.concurrent.Await
import model.Alligator
import scala.concurrent.duration.Duration
import com.dius.pact.consumer.PactVerification.PactVerified

/**
 * Split out the one request ,
 * just to demonstrate that you can have a pact defined over multiple specifications
 */
class SpecificAnimalServiceSpec extends Specification {
  "Animal Service" should {
    val animalPact = MakePact()
      .withProvider("Animal_Serice")
      .withConsumer("Zoo_App")

    val port = 9000
    val baseUrl = s"http://localhost:$port"

    val animalService = new AnimalService(baseUrl)
    val executionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
    val timeout = Duration(1, "s")

    "Find Mary By Name" in {
      val state = "there is an alligator named Mary"
      val pact: Pact = animalPact.withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for alligator Mary",
            method = "GET",
            path = "/alligators/Mary")
          .willRespondWith(
            200,
            Map[String, String](),
            """ {"name": "Mary"} """)
        )

      pact.runConsumer(PactServerConfig(port), state) {
        Await.result(animalService.findAlligator("Mary")(executionContext), timeout) must beSome(Alligator("Mary"))
      } must beEqualTo(PactVerified).await
    }

    "Handle when Mary does not exist" in {
      val state = "there is not an alligator named Mary"
      val pact: Pact = animalPact.withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for alligator Mary",
            method = "GET",
            path = "/alligators/Mary")
          .willRespondWith(
            404,
            None,
            None)
      )

      pact.runConsumer(PactServerConfig(port), state) {
        Await.result(animalService.findAlligator("Mary")(executionContext), timeout) must beNone
      } must beEqualTo(PactVerified).await
    }
  }
}
