import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.Keys.packageName

object Settings {

  val docker =
    List(
      dockerExposedPorts := Seq(8080),
      dockerBaseImage := "openjdk:11-jdk-slim",
      dockerRepository := Some("wagewise-ml"),
      Docker / packageName := "wagewise-ml",
      dockerUpdateLatest := true,
//      dockerBuildCommand += "COPY src/main/resources/employments.csv opt/docker/employments.csv",
//      dockerBuildOptions += "COPY src/main/resources/employments.csv opt/docker/employments.csv",
//      dockerCommands := dockerCommands.value :+ Seq(Cmd("COPY", "modules/ml/infrastructure/src/main/resources/employments.csv", "/opt/docker/employments.csv")),
//      dockerCommands += Cmd("COPY", "modules/ml/infrastructure/src/main/resources/employments.csv", "/opt/docker/employments.csv"),
//      Universal / mappings += ((sourceDirectory.value / "modules" /"ml"/"infrastructure"/"src"/"main"/"resources"/"employments.csv"), "modules/ml/infrastructure/src/main/resources/employments.csv"),
//      dockerCommands += Cmd("COPY", "src/main/resources/employments.csv", "/opt/docker/employments.csv"),
//      dockerExecCommand += "COPY src/main/resources/employments.csv opt/docker/employments.csv",
      dockerEnvVars += "EMPLOYMENTS_DATASET_PATH" -> "/opt/docker/employments.csv"
    )
}
