package name.driscoll.twitter

import io.netty.incubator.channel.uring.IOUringEventLoopGroup
import name.driscoll.twitter.tweet.{TweetApp, TweetRepo}
import name.driscoll.twitter.users.{PersistentUserRepo, UserApp}
//import zio.http.netty.{ChannelFactories,EventLoopGroups}
//import zio.http.service.{EventLoopGroup, Server}
import zio.http._
import zio.http.model._
import zio._

import zio.http.netty._

import java.net.InetSocketAddress
import java.util.concurrent.ThreadFactory



object MainApp extends ZIOAppDefault:
  private val config = ServerConfig.default
    .copy(channelType=ChannelType.URING)
    .leakDetection(ServerConfig.LeakDetectionLevel.DISABLED)
//    .port(8090)

  private val configLayer = ServerConfig.live(config)
  private val temp = new InetSocketAddress(8080)

  def run: ZIO[Environment with ZIOAppArgs with Scope,Any,Any] = (for
    tweetRoutes <- ZIO.service[TweetApp]
    end <- zio.Fiber.dumpAllWith(d => d.prettyPrint.unless(d.status.isDone).flatMap(Console.printError(_))).delay(10.seconds).fork
    server <- Server.serve(tweetRoutes())
  yield end).provide(
//      PersistentUserRepo.layer ,
    TweetApp.layer,
    TweetRepo.persistent,
    Server.live,
    configLayer,
//    ZLayer.fromZIO { EventLoopGroups.make(ZIO.succeed(new IOUringEventLoopGroup(1, null.asInstanceOf[ThreadFactory], 0, 500000)))},

  )
