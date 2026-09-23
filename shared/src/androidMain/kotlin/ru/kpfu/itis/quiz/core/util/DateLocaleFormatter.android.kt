package ru.kpfu.itis.quiz.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

actual fun formatDateWithLocale(epochDays: Int, locale: AppLocale): String {
    val javaLocale = when(locale) {
        AppLocale.SYSTEM -> Locale.getDefault()
        AppLocale.EN_GB -> Locale("en", "GB")
        AppLocale.EN_US -> Locale("en", "US")
        AppLocale.RU -> Locale("ru", "RU")
    }

    val date = LocalDate.ofEpochDay(epochDays.toLong())
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(javaLocale)
    return date.format(formatter)
}
