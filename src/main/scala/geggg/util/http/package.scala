package geggg.util

package object http {

  val DEFAULT_CONNECT_TIMEOUT: Int = 10000
  val DEFAULT_READ_TIMEOUT: Int = 10000
  val DEFAULT_RESPONSE_ENCODE: String = "UTF-8"

  implicit def map2querymap(m: Map[String, Any]) = new QueryMap(m)

}
