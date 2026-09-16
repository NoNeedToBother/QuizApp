package ru.kpfu.itis.quiz.core.config

expect class PlatformConfiguration {
    val appVersionName: String
    val appVersionNumber: String
    val osVersion: String
    val deviceType: Configuration.DeviceType
}
