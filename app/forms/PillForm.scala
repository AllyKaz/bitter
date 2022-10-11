package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, number, optional, text}

object PillForm {

  val form = Form(
    mapping(
      "text" -> text,
      "replyId" -> optional(number)
    )(Data.apply)(Data.unapply)
  )

  case class Data(text: String,
                  replyId: Option[Int])


}
