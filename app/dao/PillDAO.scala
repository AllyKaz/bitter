package dao

import models.{Pill, PillLike}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.ast.ScalaBaseType.bigDecimalType
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PillDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                        (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._



  def all(): Future[Seq[Pill]] = db.run(pills.result)

  def insert(pill: Pill): Future[Unit] = db.run(pills += pill).map { _ => ()}

  def likePill(pillLike: PillLike) = db.run(pillLikes += pillLike)

  def withUsersLikesAndReplies() = {
    db.run(
      pills.filter(_.replyId.isEmpty)
        .joinLeft(pillLikes).on(_.id === _.pillID)
        .groupBy(_._1)
        .map(p => (p._1, p._2.map(t => {Case If t._2.isEmpty Then 0 Else 1}).sum))
        .join(users).on(_._1.userId === _.id)
        .joinLeft(pills).on(_._1._1.id === _.replyId)
        .groupBy((_._1))
        .map(t => (t._1._1._1, t._1._1._2, t._1._2, t._2.map(t => {Case If t._2.isEmpty Then 0 Else 1}).sum))
        .result
    )
  }

  def getWithUsersLikesAndReplies(id: Int) = {
    db.run(
      pills.filter(_.replyId === id)
        .joinLeft(pillLikes).on(_.id === _.pillID)
        .groupBy(_._1)
        .map(p => (p._1, p._2.map(t => {Case If t._2.isEmpty Then 0 Else 1}).sum))
        .join(users).on(_._1.userId === _.id)
        .joinLeft(pills).on(_._1._1.id === _.replyId)
        .groupBy((_._1))
        .map(t => (t._1._1._1, t._1._1._2, t._1._2, t._2.map(t => {Case If t._2.isEmpty Then 0 Else 1}).sum))
        .result
    )
  }










}
