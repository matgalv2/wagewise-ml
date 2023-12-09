package io.github.matgalv2.wagewise.logging

sealed trait LogLevel

object LogLevel {
  final case object Info extends LogLevel
  final case object Warn extends LogLevel
  final case object Error extends LogLevel
}
