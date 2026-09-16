package ru.kpfu.itis.quiz.android

import android.app.Application
import ru.kpfu.itis.quiz.android.config.initCommon

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initCommon()
    }
}
