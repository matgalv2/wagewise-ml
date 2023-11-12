//addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
//addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.3.3")
addSbtPlugin("com.twilio" % "sbt-guardrail" % "0.64.3")
addSbtPlugin("io.spray"   % "sbt-revolver"  % "0.9.1")

/* TODO: Check if it's needed for docker auto images with compose
  https://stackoverflow.com/questions/45531198/warnings-while-building-scala-spark-project-with-sbt
 */
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")
/* Check if needed */
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
dependencyOverrides ++= Seq(
  "org.scala-lang.modules" % "scala-xml_2.12" % "2.1.0",
)