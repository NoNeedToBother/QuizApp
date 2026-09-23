package ru.kpfu.itis.quiz.feature.users.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.kpfu.itis.quiz.core.database.AppDatabase
import ru.kpfu.itis.quiz.core.model.User
import ru.kpfu.itis.quiz.core.util.formatDateWithLocale
import ru.kpfu.itis.quiz.feature.users.domain.repository.UserRepository

class UserRepositoryImpl(
    private val database: AppDatabase
) : UserRepository {
    override suspend fun findByUsernameQueryWithPaging(
        username: String,
        limit: Int,
        offset: Int
    ): List<User> {
        return withContext(Dispatchers.IO) {
            val dao = database.userDao()

            dao.getByQueryNameWithPaging(username.plus("%"), limit, offset).map {
                User(
                    id = it.id,
                    username = it.username,
                    profilePictureUri = it.profilePictureUri,
                    info = it.info,
                    dateRegistered = formatDateWithLocale(it.dateRegistered),
                )
            }
        }
    }
}