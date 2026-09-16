package ru.kpfu.itis.quiz.core.config

actual class PlatformConfiguration(
    actual val appVersionName: String,
    actual val appVersionNumber: String,
    actual val osVersion: String,
    actual val deviceType: Configuration.DeviceType,
)
