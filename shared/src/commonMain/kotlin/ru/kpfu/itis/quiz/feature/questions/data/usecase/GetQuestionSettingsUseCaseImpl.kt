package ru.kpfu.itis.quiz.feature.questions.data.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.kpfu.itis.quiz.core.model.QuestionSettings
import ru.kpfu.itis.quiz.feature.questions.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.quiz.feature.questions.domain.usecase.GetQuestionSettingsUseCase

class GetQuestionSettingsUseCaseImpl(
    private val questionSettingsRepository: QuestionSettingsRepository
) : GetQuestionSettingsUseCase {
    override suspend fun invoke(): QuestionSettings {
        return withContext(Dispatchers.IO) {
            val difficulty = questionSettingsRepository.getDifficulty()
            val category = questionSettingsRepository.getCategory()
            val gameMode = questionSettingsRepository.getGameMode()
            val locale = questionSettingsRepository.getLocale()
            QuestionSettings(
                difficulty, category, gameMode, locale
            )
        }
    }
}