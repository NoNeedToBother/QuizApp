package ru.kpfu.itis.quiz.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import ru.kpfu.itis.quiz.core.config.Configuration
import ru.kpfu.itis.quiz.core.database.dao.ResultDao
import ru.kpfu.itis.quiz.core.database.dao.UserDao
import ru.kpfu.itis.quiz.core.database.entity.ResultEntity
import ru.kpfu.itis.quiz.core.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ResultEntity::class],
    version = 2,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resultDao(): ResultDao

    abstract fun userDao(): UserDao

}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect class AppDatabaseBuilderFactory() {

    fun getDatabaseBuilder(configuration: Configuration): RoomDatabase.Builder<AppDatabase>

}
