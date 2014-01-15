package pact

import com.dius.pact.play.PactPlaySpecification
import java.io.File
import com.dius.pact.runner.PactConfiguration
import play.api.test.FakeApplication

class PactProviderSpec extends PactPlaySpecification {
  def pactRoot: File = new File(this.getClass.getClassLoader.getResource("pact").getPath)
  def pactConfig: PactConfiguration = new PactConfiguration(
    providerBaseUrl = "http://localhost:9000",
    stateChangeUrl= "unused",
    timeoutSeconds = 10
  )

  def startAppInState(state: String): FakeApplication = {
    new FakeApplication(additionalConfiguration = Map("state" -> state))
  }
}
