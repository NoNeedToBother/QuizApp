package ru.kpfu.itis.quiz.core

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.kpfu.itis.quiz.core.config.Configuration
import ru.kpfu.itis.quiz.core.config.PlatformConfiguration
import ru.kpfu.itis.quiz.core.database.databaseModule
import ru.kpfu.itis.quiz.core.network.networkModule
import ru.kpfu.itis.quiz.core.settings.settingsModule
import ru.kpfu.itis.quiz.feature.authentication.di.authenticationModule
import ru.kpfu.itis.quiz.feature.leaderboard.di.leaderboardModule
import ru.kpfu.itis.quiz.feature.profile.di.profileModule
import ru.kpfu.itis.quiz.feature.questions.di.questionsModule
import ru.kpfu.itis.quiz.feature.users.di.usersModule
import ru.kpfu.itis.quiz.platformModule

object CommonKmp {

    fun initKoin(
        configuration: Configuration,
        appDeclaration: KoinAppDeclaration = {},
    ) {
        startKoin {
            appDeclaration()
            modules(
                createConfiguration(configuration),
                networkModule,
                databaseModule,
                settingsModule,
                authenticationModule,
                questionsModule,
                usersModule,
                leaderboardModule,
                profileModule,
                platformModule(),
            )
        }
    }

    private fun createConfiguration(configuration: Configuration) = module {
        single<Configuration> { configuration }
        single<PlatformConfiguration> { configuration.platformConfiguration }
    }
}
