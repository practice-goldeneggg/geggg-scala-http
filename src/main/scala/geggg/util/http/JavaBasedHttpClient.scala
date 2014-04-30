package geggg.util
package http

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.IOException
import java.io.PrintStream
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL

class JavaBasedHttpClient(cfg: HttpConfig = HttpConfig()) extends {
    val config = cfg
  } with HttpClient {

  def get(url: String,
    query: QueryMap = new QueryMap,
    enc: String = "UTF-8",
    authUser: BasicAuthUser = BasicAuthUser()): HttpResponse =
    request("GET", new URL(url + "?" + query.queries(enc)), query, authUser)

  def post(url: String,
    query: QueryMap = new QueryMap,
    enc: String = "UTF-8",
    authUser: BasicAuthUser = BasicAuthUser()): HttpResponse =
    request("POST", new URL(url), query, authUser)

  def put(url: String,
    query: QueryMap = new QueryMap,
    enc: String = "UTF-8",
    authUser: BasicAuthUser = BasicAuthUser()): HttpResponse =
    request("PUT", new URL(url), query, authUser)

  protected def request(method: String,
    url: URL,
    query: QueryMap,
    authUser: BasicAuthUser): HttpResponse = {
    try {
      BasicAuthenticator.setBasicAuthUser(authUser)
      val conn = getConnection(method, url)
      conn.connect
      val contents = getContents(conn, method, query)
      HttpResponse(conn.getResponseCode,
        conn.getResponseMessage,
        conn.getContentType,
        conn.getContentLength,
        contents)
    } catch {
      case e: Exception => throw e
    }
  }

  protected def getConnection(method: String, url: URL): HttpURLConnection = {
    val conn: HttpURLConnection = url.openConnection.asInstanceOf[HttpURLConnection]
    conn.setConnectTimeout(config.connTimeOut)
    conn.setReadTimeout(config.readTimeOut)
    config.requestHeaders.foreach { case(key, value) => conn.setRequestProperty(key, value) }
    conn.setRequestMethod(method)
    method match {
      case "GET" => conn.setDoInput(true)
      case "POST" => conn.setDoOutput(true)
      case "PUT" => conn.setDoOutput(true)
    }

    conn
  }

  protected def getContents(connectedConn: HttpURLConnection,
    method: String,
    query: QueryMap): String = method match {
    case "GET" => doInput(connectedConn)
    case "POST" => doOutput(connectedConn, query)
    case "PUT" => doOutput(connectedConn, query)
  }

  protected def doInput(conn: HttpURLConnection): String = {
    var is: InputStream = null
    var baos = new ByteArrayOutputStream
    try {
      is = conn.getInputStream
      inputRecursive(is, baos)
    } catch {
      case e: Exception => throw e
    } finally {
      if (is != null) is.close
      baos.close
    }
  }

  private def inputRecursive(is: InputStream, baos: ByteArrayOutputStream): String = {
    var byteArray = new Array[Byte](1024)
    val size = is.read(byteArray)
    if (size == -1) {
      new String(baos.toByteArray(), config.responseEncode)
    } else {
      baos.write(byteArray, 0, size)
      inputRecursive(is, baos)
    }
  }

  protected def doOutput(conn: HttpURLConnection, query: QueryMap): String = {
    var ps: PrintStream = null
    try {
      val os = conn.getOutputStream
      ps = new PrintStream(os)
      ps.print(query.queries())
      doInput(conn)
    } catch {
      case e: Exception => throw e
    } finally {
      if (ps != null) ps.close
    }
  }

}
