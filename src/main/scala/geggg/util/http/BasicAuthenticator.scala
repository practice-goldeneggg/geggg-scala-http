package geggg.util
package http

case class BasicAuthUser(val userName: String = "", val password: String = "")

object BasicAuthenticator {

  def setBasicAuthUser(authUser: BasicAuthUser) {
    if (authUser.userName != "" && authUser.password != "") {
      import java.net.{Authenticator, PasswordAuthentication}
      Authenticator.setDefault(
        new Authenticator {
          override def getPasswordAuthentication =
            new PasswordAuthentication(authUser.userName, authUser.password.toCharArray)
        }
      )
    }
  }

}
