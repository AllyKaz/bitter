package dao

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.{PasswordHasher, PasswordInfo}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.SignUpForm
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class UserDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, passwordHasher: PasswordHasher,
                         authInfoRepository: AuthInfoRepository)
                        (implicit executionContext: ExecutionContext) extends IdentityService[User] with HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  def insert(user: User) = db.run(users += user).map{ _ => ()}

  def all(): Future[Seq[User]] = db.run(users.result)

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = db.run(
    users
      .filter(user => user.providerKey === loginInfo.providerKey && user.providerID === loginInfo.providerID)
      .result
      .headOption)

  def create(data: SignUpForm.Data): Future[LoginInfo] = {

    val user = User(
      id = None,
      username = data.username,
      providerID = CredentialsProvider.ID,
      providerKey = data.username)

    db.run {
      (users returning users.map(_.id)) += user
    } andThen {
      case Failure(_: Throwable) => None
      case Success(id) => {
        val loginInfo: LoginInfo = LoginInfo(CredentialsProvider.ID, data.username)
        val authInfo: PasswordInfo = passwordHasher.hash(data.password)
        authInfoRepository.add(loginInfo, authInfo)
      }
    } map { _id => LoginInfo(CredentialsProvider.ID, user.username) }

  }


}
