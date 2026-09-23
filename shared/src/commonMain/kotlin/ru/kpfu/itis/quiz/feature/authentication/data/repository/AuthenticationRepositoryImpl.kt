package ru.kpfu.itis.quiz.feature.authentication.data.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import ru.kpfu.itis.quiz.core.database.AppDatabase
import ru.kpfu.itis.quiz.core.database.entity.UserEntity
import ru.kpfu.itis.quiz.core.model.User
import ru.kpfu.itis.quiz.core.util.encrypt
import ru.kpfu.itis.quiz.feature.authentication.data.mapper.nullableEntityToUser
import ru.kpfu.itis.quiz.feature.authentication.domain.repository.AuthenticationRepository

internal class AuthenticationRepositoryImpl(
    private val appDatabase: AppDatabase,
) : AuthenticationRepository {

    override suspend fun register(username: String, password: String, confirmPassword: String): Boolean {
        val dao = appDatabase.userDao()

        val registerDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val userEntity = UserEntity(
            username = username,
            password = password.encrypt(),
            dateRegistered = registerDate.toEpochDays(),
            isSignedIn = true
        )
        return dao.save(userEntity) != -1L
    }

    override suspend fun signIn(username: String, password: String): Boolean {
        val dao = appDatabase.userDao()

        val user = dao.getByUsernameAndPassword(username, password.encrypt())
        return if (user != null) {
            dao.update(user.copy(isSignedIn = true))
            true
        } else false
    }

    override suspend fun getSignedInUser(): User? {
        val dao = appDatabase.userDao()

        return nullableEntityToUser(dao.getSignedInUser())
    }
}
