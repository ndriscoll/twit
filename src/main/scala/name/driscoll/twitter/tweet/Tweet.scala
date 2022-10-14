package name.driscoll.twitter.tweet

import name.driscoll.twitter.users.User
import zio.json.*

import java.time.Instant

case class Tweet(author: User, body: String, createdAt: Instant)

object Tweet:
  given JsonEncoder[Tweet] =
    DeriveJsonEncoder.gen[Tweet]
  given JsonDecoder[Tweet] =
    DeriveJsonDecoder.gen[Tweet]


