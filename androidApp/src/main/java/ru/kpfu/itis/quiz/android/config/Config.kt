package ru.kpfu.itis.quiz.android.config

import android.content.res.Resources
import android.os.Build
import org.koin.android.ext.koin.androidContext
import ru.kpfu.itis.quiz.android.App
import ru.kpfu.itis.quiz.android.BuildConfig
import ru.kpfu.itis.quiz.core.CommonKmp
import ru.kpfu.itis.quiz.core.config.Configuration
import ru.kpfu.itis.quiz.core.config.PlatformConfiguration

internal fun App.initCommon() {
    val config = Configuration(
        platformConfiguration = PlatformConfiguration(
            androidContext = applicationContext,
            appVersionName = BuildConfig.VERSION_NAME,
            appVersionNumber = BuildConfig.VERSION_CODE.toString(),
            osVersion = Build.VERSION.RELEASE.toString(),
            deviceType = resources.deviceType,
        ),
        isDebug = BuildConfig.DEBUG,
        isHttpLoggingEnabled = BuildConfig.DEBUG,
    )
    CommonKmp.initKoin(config) {
        androidContext(applicationContext)
    }
}

private val Resources.deviceType: Configuration.DeviceType
    get() = when {
        displayMetrics.widthPixels > 600.dp -> Configuration.DeviceType.Tablet
        else -> Configuration.DeviceType.Phone
    }

private inline val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
