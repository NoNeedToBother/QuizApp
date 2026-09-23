package ru.kpfu.itis.quiz.core.util

import android.util.Log

actual fun log(tag: String, message: String) {
    Log.i(tag, message)
}

actual fun logError(tag: String, message: String) {
    Log.e(tag, message)
}
