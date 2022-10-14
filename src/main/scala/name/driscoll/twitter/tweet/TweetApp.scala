package name.driscoll.twitter.tweet

import zhttp.http.*
import zio.*
import zio.json.*

/**
 * An http app that: 
 *   - Accepts a `Request` and returns a `Response`
 *   - Does not fail
 *   - Does not use the environment
 */
case class TweetApp(repo: TweetRepo):
  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      // POST /tweets
      case req@(Method.POST -> !! / "tweets") =>
        for
          t <- req.body.asString
          r <- repo.create(t).map(id => Response.text(id.toString))

        yield r
      case Method.GET -> !! / "tweets" / id =>
      repo.lookup(id)
        .map {
          case Some(tweet) =>
            Response.json(tweet.toJson)
          case None =>
            Response.status(Status.NotFound)
        }
      // GET /users
      case Method.GET -> !! / "tweets" =>
        repo.tweets.map(response => Response.json(response.toJson))
    }


object TweetApp:
  def layer: ZLayer[TweetRepo, Throwable, TweetApp] = ZLayer.fromFunction(TweetApp(_))