package ru.kpfu.itis.quiz.feature.profile.data.mapper

import ru.kpfu.itis.quiz.core.database.entity.ResultEntity
import ru.kpfu.itis.quiz.core.database.entity.UserEntity
import ru.kpfu.itis.quiz.core.util.formatDateWithLocale
import ru.kpfu.itis.quiz.feature.profile.domain.model.Result
import ru.kpfu.itis.quiz.feature.profile.domain.model.User
import ru.kpfu.itis.quiz.feature.profile.domain.model.UserWithResults

typealias UserEntityWithResults = ru.kpfu.itis.quiz.core.database.entity.UserWithResults

fun mapUserEntityWithResults(entity: UserEntityWithResults): UserWithResults {
    return UserWithResults(
        user = mapUserEntity(entity.user),
        results = entity.results.map { mapResultEntity(it) }
    )
}

private fun mapUserEntity(entity: UserEntity): User {
    return User(
        id = entity.id,
        username = entity.username,
        profilePictureUri = entity.profilePictureUri,
        info = entity.info,
        dateRegistered = formatDateWithLocale(entity.dateRegistered),
    )
}

private fun mapResultEntity(result: ResultEntity): Result {
    return Result(
        id = result.id, score = result.score
    )
}
