package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.{PillDAO, UserDAO}
import forms.PillForm
import models.{DTO, DefaultEnv, Pill, PillDTO, PillLike, User}
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.mvc._

import java.time.format.{DateTimeFormatter, FormatStyle}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

import scala.None.getOrElse
import scala.util.{Failure, Success}


@Singleton
class HomeController @Inject()(controllerComponents: MessagesControllerComponents, pillDAO: PillDAO, userDAO: UserDAO,
                               silhouette: Silhouette[DefaultEnv],
                               credentialsProvider: CredentialsProvider)
  (implicit ec: ExecutionContext)
  extends AbstractController(controllerComponents) with I18nSupport {


  val authService: AuthenticatorService[CookieAuthenticator] = silhouette.env.authenticatorService


  def index(): Action[AnyContent] =  silhouette.UserAwareAction.async { implicit request =>
    pillDAO.withUsersLikesAndReplies().map(pills => Ok(views.html.index(
      pills.map(p => DTO(mapToDTO(p._1), p._2, p._3, p._4)),
      PillForm.form, request.identity)))
  }


  def pillReplies(id: Int) = silhouette.UserAwareAction.async { implicit request =>

    pillDAO.getWithUsersLikesAndReplies(id).map(pills => Ok(views.html.index(
      pills.map(p => DTO(mapToDTO(p._1), p._2, p._3, p._4)),
      PillForm.form, request.identity)))

  }

  def pillForm: Action[AnyContent] = silhouette.SecuredAction { implicit request =>
    Ok(views.html.pillForm(PillForm.form, Some(request.identity)))
  }

  def postPillForm: Action[AnyContent] = silhouette.SecuredAction.async { implicit request =>
    PillForm.form.bindFromRequest.fold(
      (hasErrors: Form[PillForm.Data]) => Future.successful(BadRequest(views.html.pillForm(hasErrors, Some(request.identity)))),
      (success: PillForm.Data) => {
        pillDAO.insert(Pill(None, success.text, request.identity.id, success.replyId)).map(_ => Redirect(routes.HomeController.index()))
      }
    )
  }

  def likePill = silhouette.SecuredAction.async { implicit request =>
    val jsonBody = request.body.asJson
    jsonBody.map( json => pillDAO.likePill(PillLike((json \ "id").as[Int], (json \ "pillId").as[Int]))
      .map( _ => Ok("Liked"))).getOrElse(Future.successful(BadRequest("Bad Request")))

  }

  val mapToDTO = (p: Pill) =>
    PillDTO(p.id, p.text, p.userId, p.replyId, p.date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)))




}
