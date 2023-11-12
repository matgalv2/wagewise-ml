package io.github.matgalv2.employment.model

sealed abstract class CoreProgrammingLanguage(val value: String)

object CoreProgrammingLanguage {
  case object Swift extends CoreProgrammingLanguage("Swift")
  case object Python extends CoreProgrammingLanguage("Python")
  case object Java extends CoreProgrammingLanguage("Java")
  case object JavaScript extends CoreProgrammingLanguage("JavaScript")
  case object R extends CoreProgrammingLanguage("R")
  case object ObjectiveC extends CoreProgrammingLanguage("Objective-C")
  case object Kotlin extends CoreProgrammingLanguage("Kotlin")
  case object Php extends CoreProgrammingLanguage("PHP")
  case object Cobol extends CoreProgrammingLanguage("Cobol")
  case object Go extends CoreProgrammingLanguage("Go")
  case object Other extends CoreProgrammingLanguage("Other")
  case object Ruby extends CoreProgrammingLanguage("Ruby")
  }
