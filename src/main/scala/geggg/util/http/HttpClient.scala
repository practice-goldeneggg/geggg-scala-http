package geggg.util
package http

case class HttpConfig(val connTimeOut: Int = DEFAULT_CONNECT_TIMEOUT,
  val readTimeOut: Int = DEFAULT_READ_TIMEOUT,
  val responseEncode: String = DEFAULT_RESPONSE_ENCODE,
  val requestHeaders: Map[String,String] = Map.empty)

case class HttpResponse(val status: Int,
  val message: String,
  val contentType: String = "",
  val contentLength: Int = 0,
  val contents: String = "")

trait HttpClient {

  val config: HttpConfig

  def get(url: String,
    query: QueryMap = new QueryMap,
    enc: String = "UTF-8",
    authUser: BasicAuthUser = BasicAuthUser()): HttpResponse

  def post(url: String,
    query: QueryMap = new QueryMap,
    enc: String = "UTF-8",
    authUser: BasicAuthUser = BasicAuthUser()): HttpResponse

  def put(url: String,
    query: QueryMap = new QueryMap,
    enc: String = "UTF-8",
    authUser: BasicAuthUser = BasicAuthUser()): HttpResponse

}

object HttpClient {

  val CLIENT_NAME_JAVA_BASED = "java_based"

  def apply(config: HttpConfig = HttpConfig(), name: String = CLIENT_NAME_JAVA_BASED): HttpClient =
    name match {
      case CLIENT_NAME_JAVA_BASED => new JavaBasedHttpClient(config)
      case _ => new JavaBasedHttpClient(config) // XXX
    }

}
