package ru.kpfu.itis.quiz.feature.questions.presentation.settings.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale
import ru.kpfu.itis.quiz.core.util.currentLocale
import ru.kpfu.itis.quiz.core.util.toEnumName
import ru.kpfu.itis.quiz.feature.questions.domain.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.quiz.feature.questions.domain.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mapper.mapQuestionSettings
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.model.QuestionSettings
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi.QuestionSettingsScreenIntent
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi.QuestionSettingsScreenSideEffect
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi.QuestionSettingsScreenState

class QuestionSettingsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase,
): ViewModel(), ContainerHost<QuestionSettingsScreenState, QuestionSettingsScreenSideEffect> {

    override val container = container<QuestionSettingsScreenState, QuestionSettingsScreenSideEffect>(
        initialState = QuestionSettingsScreenState(),
        savedStateHandle = savedStateHandle,
        serializer = QuestionSettingsScreenState.serializer()
    )

    fun onIntent(intent: QuestionSettingsScreenIntent) {
        when(intent) {
            is QuestionSettingsScreenIntent.GetQuestionSettings -> getQuestionSettings()
            is QuestionSettingsScreenIntent.SaveQuestionSettings -> saveQuestionSettings()
            is QuestionSettingsScreenIntent.UpdateCategory -> updateCategory(intent)
            is QuestionSettingsScreenIntent.UpdateDifficulty -> updateDifficulty(intent)
            is QuestionSettingsScreenIntent.UpdateGameMode -> updateGameMode(intent)
            is QuestionSettingsScreenIntent.UpdateLocale -> updateLocale(intent)
        }
    }

    private fun getQuestionSettings() = intent {
        val questionSettings =
            mapQuestionSettings(getQuestionSettingsUseCase.invoke())
        reduce { state.copy(settings = questionSettings) }
    }

    private fun saveQuestionSettings() = intent {
        saveQuestionSettingsUseCase.invoke(
            difficulty = state.settings?.difficulty,
            category = state.settings?.category,
            gameMode = state.settings?.gameMode,
            locale = state.settings?.locale
        )
        state.settings?.let { currentLocale = it.locale }
    }

    private fun updateCategory(intent: QuestionSettingsScreenIntent.UpdateCategory) = intent {
        val newSettings = QuestionSettings(
            difficulty = state.settings?.difficulty ?: Difficulty.EASY,
            category = Category.valueOf(intent.category.toEnumName()),
            gameMode = state.settings?.gameMode ?: GameMode.BLITZ,
            locale = state.settings?.locale ?: AppLocale.SYSTEM,
        )
        reduce { state.copy(settings = newSettings) }
    }

    private fun updateDifficulty(intent: QuestionSettingsScreenIntent.UpdateDifficulty) = intent {
        val newSettings = QuestionSettings(
            difficulty = Difficulty.valueOf(intent.difficulty.toEnumName()),
            category = state.settings?.category ?: Category.GENERAL,
            gameMode = state.settings?.gameMode ?: GameMode.BLITZ,
            locale = state.settings?.locale ?: AppLocale.SYSTEM,
        )
        reduce { state.copy(settings = newSettings) }
    }

    private fun updateGameMode(intent: QuestionSettingsScreenIntent.UpdateGameMode) = intent {
        val newSettings = QuestionSettings(
            difficulty = state.settings?.difficulty ?: Difficulty.EASY,
            category = state.settings?.category ?: Category.GENERAL,
            gameMode = GameMode.valueOf(intent.gameMode.toEnumName()),
            locale = state.settings?.locale ?: AppLocale.SYSTEM,
        )
        reduce { state.copy(settings = newSettings) }
    }

    private fun updateLocale(intent: QuestionSettingsScreenIntent.UpdateLocale) = intent {
        val newSettings = QuestionSettings(
            difficulty = state.settings?.difficulty ?: Difficulty.EASY,
            category = state.settings?.category ?: Category.GENERAL,
            gameMode = state.settings?.gameMode ?: GameMode.BLITZ,
            locale = intent.locale,
        )
        reduce { state.copy(settings = newSettings) }
    }
}
