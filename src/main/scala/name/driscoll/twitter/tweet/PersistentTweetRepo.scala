package name.driscoll.twitter.tweet

import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.{Escape, H2ZioJdbcContext}
import io.getquill.jdbczio.Quill
import io.getquill.*
import name.driscoll.twitter.MainApp.Environment
import name.driscoll.twitter.users.{User, Users}
import zio.*

import java.time.Instant
import java.util.UUID
import javax.sql.DataSource

case class Tweets(id: Long, authorId: Long, body: String, createdAt: Instant)

case class PersistentTweetRepo(ds: DataSource) extends TweetRepo:
  val ctx = new PostgresZioJdbcContext(SnakeCase)

  import ctx._
//  inline private implicit def userSchemaMeta: SchemaMeta[TweetTable] = schemaMeta[TweetTable]("tweets")//, _.authorId -> "author_id", _.createdAt -> "created_at")



  override def create(body: String): Task[Long] = {
    for
      id <- Random.nextLong
      _ <- ctx.run {
        quote {
          query[Tweets].insertValue {
            lift(Tweets(0, 1, body, Instant.now()))
          }.returningGenerated(_.id)
        }
      }
    yield id
  }.provide(ZLayer.succeed(ds))

  override def lookup(id: String): Task[Option[Tweet]] =
    ctx.run {
      quote {
        query[Tweets].join(query[Users]).on(_.authorId==_.id)
          .filter((t, u) => t.id == lift(id.toLong))
          .map((t,u) => Tweet(author = User(u.name), body = t.body, createdAt = t.createdAt))
      }
    }.provide(ZLayer.succeed(ds)).map(_.headOption)

  override def tweets: Task[List[Tweet]] =
    ctx.run {
      quote {
        query[Tweets].join(query[Users]).on(_.authorId==_.id)
          .map((t,u) => Tweet(author = User(u.name), body = t.body, createdAt = t.createdAt))
      }
    }.provide(ZLayer.succeed(ds))

object DBTestApp extends ZIOAppDefault:
  def run: ZIO[Environment with ZIOAppArgs with Scope,Any,Any] = (for
    response <- ZIO.serviceWithZIO[TweetRepo](_.create("Hello, Tweeter!"))
  yield response).provide(
    TweetRepo.persistent,
  )
