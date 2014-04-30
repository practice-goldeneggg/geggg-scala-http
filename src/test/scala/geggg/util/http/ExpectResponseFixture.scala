package geggg.util
package http

/**
 * Response fixture
 */
object ExpectResponseFixture {

  trait ExpectResponse {
    val expectRes: HttpResponse
  }

  trait ExpectResponse200Html extends ExpectResponse {
    val expectRes: HttpResponse = HttpResponse(200, "messageOK", "text/html", 0, "OK")
  }

  trait ExpectResponse302Html extends ExpectResponse {
    val expectRes: HttpResponse = HttpResponse(302, "messageFound", "text/html", 0, "Found")
  }

}
