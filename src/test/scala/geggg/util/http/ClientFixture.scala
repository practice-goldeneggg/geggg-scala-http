package geggg.util
package http

import com.github.kristofa.test.http._

/**
 * Client fixture
 */
object ClientFixture {

  val UA_IPHONE = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11B554a"

  trait Client {
    val client: HttpClient
    val method: Method
    val reqFunc: (String, QueryMap, String, BasicAuthUser) => HttpResponse
  }

  /**
   * Method type for Client fixture
   */
  trait Get extends Client {
    val method: Method = Method.GET
    val reqFunc: (String, QueryMap, String, BasicAuthUser) => HttpResponse = 
      client.get(_, _, _, _)
  }

  trait Post extends Client {
    val method: Method = Method.POST
    val reqFunc: (String, QueryMap, String, BasicAuthUser) => HttpResponse =
      client.post(_, _, _, _)
  }

  trait Put extends Client {
    val method: Method = Method.PUT
    val reqFunc: (String, QueryMap, String, BasicAuthUser) => HttpResponse =
      client.put(_, _, _, _)
  }

  /**
   * Connection configuration for Client fixture
   */
  trait DefaultConfig extends Client {
    val client: HttpClient = HttpClient()
  }

  trait CustomConfig extends Client {

    private val CUSTOM_CONN_TIMEOUT = 7000
    private val CUSTOM_READ_TIMEOUT = 6000
    private val CUSTOM_RES_ENCODING = "UTF-8"
    val CUSTOM_USER_AGENT = UA_IPHONE

    val client: HttpClient = HttpClient(
      HttpConfig(CUSTOM_CONN_TIMEOUT,
        CUSTOM_READ_TIMEOUT,
        CUSTOM_RES_ENCODING,
        Map("User-Agent" -> CUSTOM_USER_AGENT)
      ),
      HttpClient.CLIENT_NAME_JAVA_BASED
    )
  }

  trait SjisResponseConfig extends Client {
    val client: HttpClient = HttpClient(
      HttpConfig(DEFAULT_CONNECT_TIMEOUT,
        DEFAULT_READ_TIMEOUT,
        "Shift_JIS",
        Map.empty)
    )
  }

  trait NotImplementClient extends Client {
    val client: HttpClient = HttpClient(HttpConfig(), "notimplement")
  }

}
