package io.udash
package rest.jetty

import io.udash.rest.raw.RawRest.HandleRequest
import io.udash.rest.{RestApiTestScenarios, ServletBasedRestApiTest}
import org.eclipse.jetty.client.HttpClient

final class JettyRestCallTest extends ServletBasedRestApiTest with RestApiTestScenarios {
  /**
   * Similar to the default HttpClient, but with a connection timeout
   * significantly exceeding the value of the CallTimeout
   */
  val client: HttpClient = new HttpClient() {
    setMaxConnectionsPerDestination(MaxConnections)
    setIdleTimeout(IdleTimout.toMillis)
  }

  def clientHandle: HandleRequest =
    JettyRestClient.asHandleRequest(client, s"$baseUrl/api", maxPayloadSize)

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    client.start()
  }

  override protected def afterAll(): Unit = {
    client.stop()
    super.afterAll()
  }
}
