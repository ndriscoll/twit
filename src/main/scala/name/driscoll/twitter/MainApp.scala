package name.driscoll.twitter

import name.driscoll.twitter.tweet.{TweetApp, TweetRepo}
import name.driscoll.twitter.users.{PersistentUserRepo, UserApp}
import zhttp.service.Server
import zio.*

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment with ZIOAppArgs with Scope,Any,Any] = (for
    tweetRoutes <- ZIO.service[TweetApp]
    server <- Server.start(
      port = 8080,
      http = tweetRoutes() ++ UserApp()
    )
  yield server).provide(
      // An layer responsible for storing the state of the `counterApp`
      ZLayer.fromZIO(Ref.make(0)),
      
      // To use the persistence layer, provide the `PersistentUserRepo.layer` layer instead
      PersistentUserRepo.layer ,
    TweetApp.layer,
    TweetRepo.persistent,
  )
