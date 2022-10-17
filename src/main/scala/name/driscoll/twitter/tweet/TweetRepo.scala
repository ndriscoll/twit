package name.driscoll.twitter.tweet

import io.getquill.jdbczio.Quill
import zio.*

trait TweetRepo:
  def create(body: String): Task[Long]

  def lookup(id: String): Task[Option[Tweet]]
  
  def tweets: Task[List[Tweet]]


object TweetRepo:
  def persistent: ZLayer[Any, Throwable, TweetRepo] = PersistentTweetRepo.layer