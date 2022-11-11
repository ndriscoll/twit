package name.driscoll.twitter.tweet

import zio.http.*
import zio.*
import zio.json.*

import zio.http.model._

/**
 * An http app that: 
 *   - Accepts a `Request` and returns a `Response`
 *   - Does not fail
 *   - Does not use the environment
 */
case class TweetApp(repo: TweetRepo):
  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case req@(Method.POST -> !! / "slow") =>
        for
          t <- req.body.asString
          r <- repo.createSlow(t).map(id => Response.ok)

        yield r

      case req@(Method.POST -> !! / "tweets") =>
        for
          t <- req.body.asString
          r <- repo.create(t).map(id => Response.text(id.toString))
        yield r
      case req@(Method.POST -> !! / "ok") =>
        ZIO.succeed(Response.ok)

      case Method.GET -> !! / "tweets" / id =>
      repo.lookup(id)
        .map {
          case Some(tweet) =>
            Response.json(tweet.toJson)
          case None =>
            Response.status(Status.NotFound)
        }

      case Method.GET -> !! / "tweets" =>
        repo.tweets.map(response => Response.json(response.toJson))
    } ++ Http.collect[Request] {
      case req@(Method.POST -> !! / "okFast") => Response.ok
    }

object TweetApp:
  def layer: ZLayer[TweetRepo, Throwable, TweetApp] = ZLayer.fromFunction(TweetApp(_))
