package io.github.matgalv2.employment.model

sealed trait CoreProgrammingLanguage
object CoreProgrammingLanguage {
  final case object Swift extends CoreProgrammingLanguage
  final case object Python extends CoreProgrammingLanguage
  final case object Java extends CoreProgrammingLanguage
  final case object JavaScript extends CoreProgrammingLanguage
  final case object R extends CoreProgrammingLanguage
  final case object ObjectiveC extends CoreProgrammingLanguage
  final case object Kotlin extends CoreProgrammingLanguage
  final case object Php extends CoreProgrammingLanguage
  final case object Cobol extends CoreProgrammingLanguage
  final case object Go extends CoreProgrammingLanguage
  final case object Other extends CoreProgrammingLanguage
  final case object Ruby extends CoreProgrammingLanguage
}
