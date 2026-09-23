package ru.kpfu.itis.quiz.feature.questions.data.repository

import com.russhwolf.settings.Settings
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale
import ru.kpfu.itis.quiz.core.util.toEnumName
import ru.kpfu.itis.quiz.feature.questions.domain.repository.QuestionSettingsRepository

internal class QuestionSettingsRepositoryImpl(
    private val settings: Settings
): QuestionSettingsRepository {

    override fun getDifficulty(): Difficulty {
        return getString(DIFFICULTY_KEY)?.let { Difficulty.valueOf(it.toEnumName()) }
            ?: getDefaultDifficulty()
    }

    override fun saveDifficulty(difficulty: Difficulty) {
        saveString(DIFFICULTY_KEY, difficulty.name)
    }

    override fun getCategory(): Category {
        return getString(CATEGORY_KEY)?.let { Category.valueOf(it.toEnumName()) }
            ?: getDefaultCategory()
    }

    override fun saveCategory(category: Category) {
        saveString(CATEGORY_KEY, category.name)
    }

    override fun getGameMode(): GameMode {
        return getString(GAME_MODE_KEY)?.let { GameMode.valueOf(it.toEnumName()) }
            ?: getDefaultGameMode()
    }

    override fun saveGameMode(gameMode: GameMode) {
        saveString(GAME_MODE_KEY, gameMode.name)
    }

    override fun getLocale(): AppLocale {
        return getString(LOCALE_KEY)?.let { AppLocale.valueOf(it.toEnumName()) }
            ?: getDefaultLocale()
    }

    override fun saveLocale(locale: AppLocale) {
        saveString(LOCALE_KEY, locale.name)
    }

    private fun getString(key: String): String? {
        return settings.getStringOrNull(key)
    }

    private fun saveString(key: String, str: String) {
        settings.putString(key, str)
    }

    private fun getDefaultDifficulty(): Difficulty = Difficulty.MEDIUM

    private fun getDefaultCategory(): Category = Category.GENERAL

    private fun getDefaultGameMode(): GameMode = GameMode.BLITZ

    private fun getDefaultLocale(): AppLocale = AppLocale.SYSTEM

    companion object {
        private const val CATEGORY_KEY = "category"
        private const val DIFFICULTY_KEY = "difficulty"
        private const val GAME_MODE_KEY = "game_mode"
        private const val LOCALE_KEY = "locale"
    }
}
