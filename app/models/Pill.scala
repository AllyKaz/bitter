package models

import java.time.{LocalDateTime, ZonedDateTime}


case class Pill(id: Option[Int], text: String, userId: Option[Int], replyId: Option[Int], date:  ZonedDateTime = ZonedDateTime.now())




