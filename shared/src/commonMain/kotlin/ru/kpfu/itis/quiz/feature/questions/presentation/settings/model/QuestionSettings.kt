package ru.kpfu.itis.quiz.feature.questions.presentation.settings.model

import kotlinx.serialization.Serializable
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale

@Serializable
data class QuestionSettings(
    val difficulty: Difficulty,
    val category: Category,
    val gameMode: GameMode,
    val locale: AppLocale,
)
