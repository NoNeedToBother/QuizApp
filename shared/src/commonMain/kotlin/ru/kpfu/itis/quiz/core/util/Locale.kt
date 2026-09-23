package ru.kpfu.itis.quiz.core.util

var currentLocale = AppLocale.SYSTEM

enum class AppLocale {
    SYSTEM, EN_GB, EN_US, RU
}

expect fun formatDateWithLocale(epochDays: Int, locale: AppLocale = currentLocale): String
