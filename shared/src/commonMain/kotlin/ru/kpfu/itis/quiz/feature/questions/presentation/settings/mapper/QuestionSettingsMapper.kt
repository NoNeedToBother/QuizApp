package ru.kpfu.itis.quiz.feature.questions.presentation.settings.mapper

import ru.kpfu.itis.quiz.core.model.QuestionSettings as CommonQuestionSettings
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.model.QuestionSettings

fun mapQuestionSettings(settings: CommonQuestionSettings): QuestionSettings {
    return QuestionSettings(
        difficulty = settings.difficulty,
        category = settings.category,
        gameMode = settings.gameMode,
        locale = settings.locale,
    )
}
