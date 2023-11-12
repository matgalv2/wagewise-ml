Global / onChangedBuildSource := ReloadOnSourceChanges

//name := "wage-wise-ml"
//version := "0.1"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / name := "wagewise-ml"

// Convenience for cross-compat testing
ThisBuild / crossScalaVersions := Seq("2.12.14", "2.13.12")
ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)

val commonSettings = Seq(
  scalacOptions ++= (if (scalaVersion.value.startsWith("2.12"))
    Seq("-Ypartial-unification")
  else Nil),
  // Use zio-test runner
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  resolvers += Resolver.sonatypeRepo("snapshots"),

  // Ensure canceling `run` releases socket, no matter what
  run / fork := true,
  // Better syntax for dealing with partially-applied types
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full),
  // Better semantics for for comprehensions
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
)

//Universal / mappings += ((sourceDirectory.value / "src/main/resources/employments.csv"), "src/main/resources/employments.csv")

lazy val http = (project in file("http"))
  .settings(commonSettings)
  .settings(
    Compile / guardrailTasks += ScalaServer(file("api/mlApi.yaml"), pkg = "http.generated", framework = "http4s")
  )
  .enablePlugins(GuardrailPlugin, DockerPlugin, JavaAppPackaging)
  .settings(Settings.docker: _*)
  .settings(
    libraryDependencies ++= Seq(
      // Depend on http4s-managed cats and circe
      Dependencies.http4s.core,
      Dependencies.http4s.emberClient,
      Dependencies.http4s.emberServer,
      Dependencies.http4s.circe,
      Dependencies.http4s.dsl,
      // ZIO and the interop library
      Dependencies.zio.zio,
      Dependencies.zio.interopCats,
      Dependencies.zio.test,
      Dependencies.zio.testSbt,
      // ZIO config
      Dependencies.zio.config.core,
      Dependencies.zio.config.typesafeConfig,
      Dependencies.zio.config.magnolia,
      // Cats
      Dependencies.cats.core,
      Dependencies.cats.effect,
      Dependencies.cats.slf4jCats
    )
  )
  .settings(dependencyOverrides += Dependencies.comcast.core)
  .dependsOn(ml, employmentsDomain, employmentsInfrastructure)

lazy val ml = (project in file("/modules/ml"))
  .settings(name := "ml")
  .settings(libraryDependencies += Dependencies.spark.core)
  .settings(libraryDependencies += Dependencies.spark.mllib)

lazy val employmentsDomain = (project in file("/modules/employments/domain"))
  .settings(name := "employments-domain")
  .settings(libraryDependencies += Dependencies.zio.zio)

lazy val employmentsInfrastructure = (project in file("/modules/employments/infrastructure"))
  .settings(name := "employments-infrastructure")
  .settings(libraryDependencies += Dependencies.zio.zio)
  .dependsOn(employmentsDomain)

lazy val root = (project in file("."))
  .settings(name := "wagewise-ml")
  .aggregate(http, ml, employmentsDomain, employmentsInfrastructure)
