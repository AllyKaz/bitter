import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object bitter extends PlayModule with SingleModule {

  def scalaVersion = "2.12.2"
  def playVersion = "2.8.16"
  def twirlVersion = "1.5.1"

  object test extends PlayTests
}
