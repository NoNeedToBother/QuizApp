package ru.kpfu.itis.quiz.feature.leaderboard.data.mapper

import ru.kpfu.itis.quiz.core.database.entity.ResultAndUser
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.model.Result
import ru.kpfu.itis.quiz.core.model.User
import ru.kpfu.itis.quiz.core.util.formatDateWithLocale
import ru.kpfu.itis.quiz.core.util.toEnumName

fun mapResultAndUserEntity(entity: ResultAndUser): Result {
    val user = User(
        id = entity.user.id,
        username = entity.user.username,
        profilePictureUri = entity.user.profilePictureUri,
        info = entity.user.info,
        dateRegistered = formatDateWithLocale(entity.user.dateRegistered)
    )

    return with(entity.result) {
        Result(
            id = id,
            user = user,
            time = time,
            correct = correct,
            total = total,
            score = score,
            difficulty = Difficulty.valueOf(difficulty.toEnumName()),
            category = Category.valueOf(category.toEnumName()),
            gameMode = GameMode.valueOf(gameMode.toEnumName())
        )
    }
}