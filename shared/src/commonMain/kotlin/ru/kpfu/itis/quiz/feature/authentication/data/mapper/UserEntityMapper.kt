package ru.kpfu.itis.quiz.feature.authentication.data.mapper

import ru.kpfu.itis.quiz.core.database.entity.UserEntity
import ru.kpfu.itis.quiz.core.model.User
import ru.kpfu.itis.quiz.core.util.formatDateWithLocale

fun nullableEntityToUser(entity: UserEntity?): User? {
    return entity?.let {
        entityToUser(entity)
    }
}

fun entityToUser(entity: UserEntity): User {
    return User(
        id = entity.id,
        username = entity.username,
        profilePictureUri = entity.profilePictureUri,
        info = entity.info,
        dateRegistered = formatDateWithLocale(entity.dateRegistered),
    )
}
