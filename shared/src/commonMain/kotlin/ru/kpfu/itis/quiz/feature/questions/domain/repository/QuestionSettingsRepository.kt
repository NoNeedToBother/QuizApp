package ru.kpfu.itis.quiz.feature.questions.domain.repository

import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale

interface QuestionSettingsRepository {

    fun getDifficulty(): Difficulty

    fun saveDifficulty(difficulty: Difficulty)

    fun getCategory(): Category

    fun saveCategory(category: Category)

    fun getGameMode(): GameMode

    fun saveGameMode(gameMode: GameMode)

    fun getLocale(): AppLocale

    fun saveLocale(locale: AppLocale)

}
