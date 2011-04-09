import sbt._

class projectbuilder(info: ProjectInfo) extends DefaultProject(info) {
  val scalaToolsSnapshots = ScalaToolsSnapshots
  val scalatest = "org.scalatest" % "scalatest" % "1.3"
  val scalaj_collection = "org.scalaj" %% "scalaj-collection" % "1.0"

  override def mainClass = Some("org.performanceman.Runner")
}
