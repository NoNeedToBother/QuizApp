package ru.kpfu.itis.quiz.feature.questions.domain.usecase

import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale

interface SaveQuestionSettingsUseCase {
    suspend operator fun invoke(
        difficulty: Difficulty?,
        category: Category?,
        gameMode: GameMode?,
        locale: AppLocale?
    )
}