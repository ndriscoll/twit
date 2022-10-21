package name.driscoll.twitter

import name.driscoll.twitter.tweet.{TweetApp, TweetRepo}
import name.driscoll.twitter.users.{PersistentUserRepo, UserApp}
import zhttp.service.server.ServerChannelFactory
import zhttp.service.{EventLoopGroup, Server}
import zio.*

import java.net.InetSocketAddress

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment with ZIOAppArgs with Scope,Any,Any] = (for
    tweetRoutes <- ZIO.service[TweetApp]
    server <- ZIO.scoped {Server.make(
      Server(tweetRoutes() ++ UserApp()).withBinding(new InetSocketAddress(8080))
    ) *> ZIO.never}
  yield server).provide(
    EventLoopGroup.uring(0),
    ServerChannelFactory.uring,
      // To use the persistence layer, provide the `PersistentUserRepo.layer` layer instead
      PersistentUserRepo.layer ,
    TweetApp.layer,
    TweetRepo.persistent,
  )
