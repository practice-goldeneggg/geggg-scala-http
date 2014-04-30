package geggg.util
package http

import com.github.kristofa.test.http._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import ClientFixture._
import ExpectResponseFixture._
import RequestSettingFixture._

class HttpClientSpec extends FlatSpec with BeforeAndAfterAll {

  var responseProvider: SimpleHttpResponseProvider = _

  var mockServer: MockHttpServer = _

  override def beforeAll {
    responseProvider = new SimpleHttpResponseProvider
    mockServer = new MockHttpServer(MOCK_PORT, responseProvider)
    mockServer.start
  }

  override def afterAll {
    mockServer.stop
  }

  "Java based client" should "be initialized by JavaBased client and config contains default values" in
    new Get
    with DefaultConfig {
    client.config.connTimeOut shouldEqual 10000
    client.config.readTimeOut shouldEqual 10000
    client.config.responseEncode shouldEqual "UTF-8"
    client.config.requestHeaders shouldEqual Map.empty
    client shouldBe a [JavaBasedHttpClient]
  }

  "Custom config client" should "be initialized by config contains custom values" in
    new Get
    with CustomConfig {
    client.config.connTimeOut shouldEqual 7000
    client.config.readTimeOut shouldEqual 6000
    client.config.responseEncode shouldEqual "UTF-8"
    client.config.requestHeaders shouldEqual Map("User-Agent" -> UA_IPHONE)
    client shouldBe a [JavaBasedHttpClient]
  }

  "Apply other client name" should "be initialized by JavaBased client" in
    new Get
    with NotImplementClient {
    client shouldBe a [JavaBasedHttpClient]
  }

  /**
   * assertion method
   */
  private def assertRequest(url: String,
    query: QueryMap,
    enc: String,
    authUser: BasicAuthUser,
    expectRes: HttpResponse,
    method: Method,
    reqFunc: (String, QueryMap, String, BasicAuthUser) => HttpResponse,
    path: String = "/") {

    responseProvider
      .expect(method, path + "?" + query.queries(enc))
      .respondWith(expectRes.status, expectRes.contentType, expectRes.contents)

    val res = reqFunc(url, query, enc, authUser)
    res match {
      case HttpResponse(status, message, contentType, contentLength, contents) => {
        status shouldEqual expectRes.status
        contents shouldEqual expectRes.contents
      }
      case _ => fail("invalid response: %s".format(res))
    }
  }

  "GET using defaunt client" should "succeed to get contents" in
    new Get
    with DefaultConfig
    with DefaultRequestSetting
    with ExpectResponse200Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
  }

  "GET using client contains context path url" should "succeed to get contents" in
    new Get
    with DefaultConfig
    with ContextRequestSetting
    with ExpectResponse200Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc, "/context")
  }

  "GET using custom client with query" should "succeed to get contents" in
    new Get
    with CustomConfig
    with WithQueryRequestSetting
    with ExpectResponse200Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
  }

  "GET using default client with multibyte query" should "succeed to get contents" in
    new Get
    with DefaultConfig
    with WithMultiByteQueryRequestSetting
    with ExpectResponse200Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
  }

  // this test can't execute using mock-http-server
  // It's probably caused by SimpleHttpResponseProvider.java#L95 (in `extractAndSetQueryParams` method)
  //
  // `final List<NameValuePair> parameters = URLEncodedUtils.parse(queryParams, Charset.forName("UTF-8"));`
//  "GET using default client with sjis multibyte query and basic auth" should "succeed to get contents" in
//    new Get
//    with DefaultConfig
//    with WithFullRequestSetting
//    with ExpectResponse200Html {
//    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
//  }

  "GET and redirect using defaunt client" should "succeed return 302" in
    new Get
    with DefaultConfig
    with DefaultRequestSetting
    with ExpectResponse302Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
  }

  "POST using defaunt client" should "succeed to get contents" in
    new Post
    with DefaultConfig
    with DefaultRequestSetting
    with ExpectResponse200Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
  }

  "PUT using defaunt client" should "succeed to get contents" in
    new Put
    with DefaultConfig
    with DefaultRequestSetting
    with ExpectResponse200Html {
    assertRequest(url, query, enc, authUser, expectRes, method, reqFunc)
  }

}
