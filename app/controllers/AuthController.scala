package controllers

import com.mohiva.play.silhouette.api.services.{AuthenticatorResult, AuthenticatorService}
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.{PillDAO, UserDAO}
import forms.SignUpForm
import models.DefaultEnv
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AuthController @Inject()(cc: ControllerComponents,
                               silhouette: Silhouette[DefaultEnv],
                               credentialsProvider: CredentialsProvider,
                               pillDAO: PillDAO,
                               userDAO: UserDAO)
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with I18nSupport {

  val authService: AuthenticatorService[CookieAuthenticator] = silhouette.env.authenticatorService


  def register: Action[AnyContent] = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.userForm(SignUpForm.form)))
  }

  def postUserForm: Action[AnyContent] = silhouette.UnsecuredAction.async { implicit request =>
    SignUpForm.form.bindFromRequest.fold(
      (hasErrors: Form[SignUpForm.Data]) => Future.successful(BadRequest(views.html.userForm(hasErrors))),

      (success: SignUpForm.Data) => {

        userDAO.retrieve(LoginInfo(CredentialsProvider.ID, success.username))
          .flatMap((uo: Option[models.User]) =>
            uo.fold({
              userDAO.create(success).flatMap(authService.create(_))
                .flatMap(authService.init(_))
                .flatMap(authService.embed(_, Redirect(routes.HomeController.index())))

            })({ _ =>
              Future.successful(AuthenticatorResult(Redirect(routes.HomeController.index())))
            }))
      })
  }

  def logout = silhouette.SecuredAction.async { implicit request =>
    authService.discard(request.authenticator, Redirect(routes.HomeController.index()))
  }

  def login = silhouette.UnsecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.login(SignUpForm.form)))
  }

  def loginSubmit: Action[AnyContent] = silhouette.UnsecuredAction.async { implicit request =>

    SignUpForm.form.bindFromRequest.fold(

      hasErrors => Future.successful(BadRequest(views.html.login(hasErrors))),

      success => {

        credentialsProvider.authenticate(credentials = Credentials(success.username, success.password))
          .flatMap { loginInfo =>

            authService.create(loginInfo)
              .flatMap(authService.init(_))
              .flatMap(authService.embed(_, Redirect(routes.HomeController.index())))

          }.recover {
          case e: Exception =>
            Redirect(routes.AuthController.login).flashing("login-error" -> e.getMessage)
        }
      }
    )
  }




}
