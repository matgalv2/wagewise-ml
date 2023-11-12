import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.Keys.packageName
import com.typesafe.sbt.packager.docker.Cmd


object Settings {

  val docker =
    List(
      dockerExposedPorts := Seq(8080, 8080),
      dockerBaseImage := "openjdk:11-jdk-slim",
      dockerRepository := Some("wagewise-ml"),
      Docker / packageName := "wagewise-ml",
      dockerUpdateLatest := true,
//      dockerBuildCommand += "COPY src/main/resources/employments.csv opt/docker/employments.csv",
//      dockerBuildOptions += "COPY src/main/resources/employments.csv opt/docker/employments.csv",
//      dockerCommands := dockerCommands.value :+ Cmd("COPY", "modules/ml/src/main/resources/employments.csv", "/opt/docker/employments.csv"),
//      dockerCommands += Cmd("COPY", "src/main/resources/employments.csv", "/opt/docker/employments.csv"),
//      dockerExecCommand += "COPY src/main/resources/employments.csv opt/docker/employments.csv",
      dockerEnvVars += "EMPLOYMENTS_DATASET_PATH" -> "/opt/docker/employments.csv"
    )
}

