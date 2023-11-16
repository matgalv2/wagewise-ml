import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.Keys.packageName
import com.typesafe.sbt.packager.docker.Cmd


object Settings {

  val docker =
    List(
      dockerExposedPorts := Seq(8080),
      dockerBaseImage := "openjdk:11-jdk-slim",
      dockerRepository := Some("wagewise-ml"),
      Docker / packageName := "wagewise-ml",
      dockerUpdateLatest := true,
      dockerEnvVars += "EMPLOYMENTS_DATASET_PATH" -> "/opt/docker/employments.csv"
    )
}

