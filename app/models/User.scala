package models

import com.mohiva.play.silhouette.api.Identity


case class User(id: Option[Int], username: String, providerID: String, providerKey: String) extends Identity
