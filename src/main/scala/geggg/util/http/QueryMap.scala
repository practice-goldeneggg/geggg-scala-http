package geggg.util
package http

import java.net.URLEncoder
import scala.collection.immutable.Map

class QueryMap(wrapped: Map[String, Any] = Map.empty) {

  def queryString(key: String, enc: String = "UTF-8") =
    if (wrapped.isEmpty) ""
    else key + "=" + getWithEncode(key, enc)

  private def getWithEncode(key: String, enc: String) =
    URLEncoder.encode((wrapped.get(key).orElse(Option(""))).get.toString, enc)

  def queries(enc: String = "UTF-8") =
    if (wrapped.isEmpty) ""
    else wrapped.keysIterator.map(queryString(_, enc)).mkString("&")

}
