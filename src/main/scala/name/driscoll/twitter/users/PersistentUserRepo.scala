package name.driscoll.twitter.users

import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.{Escape, H2ZioJdbcContext}
import io.getquill.jdbczio.Quill
import io.getquill.*
import zio.*

import java.util.UUID
import javax.sql.DataSource

case class Users(id: Long, name: String)

case class PersistentUserRepo(ds: DataSource) extends UserRepo:
  val ctx = new PostgresZioJdbcContext(SnakeCase)

  import ctx._
//  inline private implicit def userSchemaMeta: SchemaMeta[UserTable] = schemaMeta[UserTable]("users")

  override def register(user: User): Task[String] = {
    for
      id <- Random.nextLong
      _ <- ctx.run {
        quote {
          query[Users].insertValue {
            lift(Users(id, user.name))
          }
        }
      }
    yield id.toString
  }.provide(ZLayer.succeed(ds))

  override def lookup(id: String): Task[Option[User]] =
    ctx.run {
      quote {
        query[Users]
          .filter(p => p.id == lift(id.toLong))
          .map(u => User(u.name))
      }
    }.provide(ZLayer.succeed(ds)).map(_.headOption)

  override def users: Task[List[User]] =
    ctx.run {
      quote {
        query[Users].map(u => User(u.name))
      }
    }.provide(ZLayer.succeed(ds))

object PersistentUserRepo:
  def layer: ZLayer[Any, Throwable, PersistentUserRepo] =
    Quill.DataSource.fromPrefix("UserApp") >>>
      ZLayer.fromFunction(PersistentUserRepo(_))
