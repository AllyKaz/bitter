package models

case class DTO(pill : PillDTO, likes: Option[Int], user: User, replies: Option[Int])
