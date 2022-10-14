package name.driscoll.twitter.users

import java.util.UUID
import zio.json.*

case class User(name: String)

object User:
  given JsonEncoder[User] =
    DeriveJsonEncoder.gen[User]
  given JsonDecoder[User] =
    DeriveJsonDecoder.gen[User]
