package geggg.util
package http

/**
 * Request setting fixture
 */
object RequestSettingFixture {

  val MOCK_PORT = 51235

  trait RequestSetting {
    val url: String
    val query: Map[String, Any]
    val enc: String
    val authUser: BasicAuthUser
  }

  trait DefaultRequestSetting extends RequestSetting {
    val url: String = "http://localhost:" + MOCK_PORT
    val query: Map[String, Any] = Map.empty
    val enc: String = "UTF-8"
    val authUser: BasicAuthUser = BasicAuthUser()
  }

  trait ContextRequestSetting extends DefaultRequestSetting {
    override val url: String = "http://localhost:" + MOCK_PORT + "/context"
  }

  trait WithQueryRequestSetting extends DefaultRequestSetting {
    override val query: Map[String, Any] = Map("q" -> "test")
  }

  trait WithMultiByteQueryRequestSetting extends DefaultRequestSetting {
    override val query: Map[String, Any] = Map("qm" -> "あいうえお")
  }

  trait WithFullRequestSetting extends DefaultRequestSetting {
    override val query: Map[String, Any] = Map("qs" -> "かきくけこ")
    override val enc: String = "Shift_JIS"
    override val authUser: BasicAuthUser = BasicAuthUser("user", "passwd")
  }

}
