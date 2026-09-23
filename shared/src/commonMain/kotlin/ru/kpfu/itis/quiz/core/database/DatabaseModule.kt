package ru.kpfu.itis.quiz.core.database

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import ru.kpfu.itis.quiz.core.config.Configuration

val databaseModule = module {
    single {
        AppDatabaseBuilderFactory()
    }

    single {
        val factory: AppDatabaseBuilderFactory = get()
        val configuration: Configuration = get()

        factory.getDatabaseBuilder(configuration)
            .addMigrations(MIGRATION_1_2)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}