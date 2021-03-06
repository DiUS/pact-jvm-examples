package pacts

import play.api.test.{WithBrowser, PlaySpecification}
import au.com.dius.pact.model.MakePact
import au.com.dius.pact.consumer.ConsumerPact._
import au.com.dius.pact.model.MakeInteraction.given
import au.com.dius.pact.consumer.PactServerConfig
import au.com.dius.pact.consumer.PactVerification.PactVerified
import org.openqa.selenium.phantomjs.PhantomJSDriver
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class JavascriptConsumerSpec extends PlaySpecification {
  "javascript app" should {
    val consumerPact = MakePact()
      .withProvider("Animal_Service")
      .withConsumer("javascript_zoo_app")

    "consume service" in new WithBrowser(webDriver = classOf[PhantomJSDriver]){
      val state = "there are alligators"
      val pact = consumerPact.withInteractions(
        given(state)
          .uponReceiving(
            description = "a request for animals",
            method = "GET",
            path = "/animals")
          .willRespondWith(
            200,
            Map("Content-Type" -> "application/json; charset=UTF-8"),
            """[{"name": "Bob"}]""")
      ).build

      val mockPort = 8888
      val timeout = 50
      val config = PactServerConfig(mockPort)
      Await.result(pact.runConsumer(config, state) {
        val page = browser.goTo("/index.html")
        page.fill(".endpoint").`with`(s"http://localhost:$mockPort")
        page.click(".getAnimals")
        page.await().atMost(timeout, TimeUnit.SECONDS).until(".animals").hasText("Bob")
        page.find(".animals").getText must beEqualTo("Bob")
      }, Duration(timeout, "s")) must beEqualTo(PactVerified)
    }
  }
}
