import models.{Pill, PillLike, User}
import slick.jdbc.MySQLProfile.api._

import java.time.ZonedDateTime

package object dao {

  class Pills(tag: Tag) extends Table[Pill](tag, "PILL") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def text = column[String]("TEXT")

    def userId = column[Int]("USER_ID")

    def replyId = column[Option[Int]]("REPLY_ID")

    def user = foreignKey("USER_FK", userId, users)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def date = column[ZonedDateTime]("DATE")

    def replied = foreignKey("REPLY_FK", replyId, pills)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def user2 = users.filter(_.id === userId)

    def * = (id.?, text, userId.?, replyId, date) <> (Pill.tupled, Pill.unapply)
  }


  class Users(tag: Tag) extends Table[User](tag, "USER") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def username = column[String]("USERNAME")

    def providerID = column[String]("PROVIDER_ID")

    def providerKey = column[String]("PROVIDER_KEY")

    def * = (id.?, username, providerID, providerKey) <> (User.tupled, User.unapply)
  }

  class PillLikes(tag: Tag) extends Table[PillLike](tag, "PILL_LIKE") {

    def pillID = column[Int]("PILL_ID")

    def userID = column[Int]("USER_ID")

    def liked = foreignKey("PILL_FK", pillID, pills)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def liker = foreignKey("USER_ID", userID, users)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def pk = primaryKey("PK_PILLLIKE", (pillID, userID))

    def * = (pillID, userID) <> (PillLike.tupled, PillLike.unapply)

  }

  case class Password(key: String,
                      hasher: String,
                      hash: String,
                      salt: Option[String])

  class Passwords(tag: Tag) extends Table[Password](tag, "PASSWORD") {
    def key = column[String]("PROVIDER_KEY", O.PrimaryKey)

    def hasher = column[String]("HASHER")

    def hash = column[String]("HASH")

    def salt = column[Option[String]]("SALT")

    def * = (key, hasher, hash, salt) <> (Password.tupled, Password.unapply)
  }

  val pills = TableQuery[Pills]
  val users = TableQuery[Users]
  val pillLikes = TableQuery[PillLikes]
  val passwordTable = TableQuery[Passwords]



}
