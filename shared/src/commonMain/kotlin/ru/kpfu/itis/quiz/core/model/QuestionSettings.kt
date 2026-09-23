package ru.kpfu.itis.quiz.core.model

import ru.kpfu.itis.quiz.core.util.AppLocale

data class QuestionSettings(
    val difficulty: Difficulty,
    val category: Category,
    val gameMode: GameMode,
    val locale: AppLocale,
)
