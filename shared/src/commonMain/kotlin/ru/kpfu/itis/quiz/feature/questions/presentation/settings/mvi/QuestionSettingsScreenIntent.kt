package ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi

import ru.kpfu.itis.quiz.core.util.AppLocale

sealed interface QuestionSettingsScreenIntent {
    data object GetQuestionSettings : QuestionSettingsScreenIntent
    data object SaveQuestionSettings : QuestionSettingsScreenIntent
    data class UpdateCategory(val category: String) : QuestionSettingsScreenIntent
    data class UpdateDifficulty(val difficulty: String) : QuestionSettingsScreenIntent
    data class UpdateGameMode(val gameMode: String) : QuestionSettingsScreenIntent
    data class UpdateLocale(val locale: AppLocale) : QuestionSettingsScreenIntent
}