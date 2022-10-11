package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}

object SignUpForm {

  val form = Form (
    mapping (
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(Data.apply)(Data.unapply)
  )

  case class Data(username: String,
                  password: String)
}


