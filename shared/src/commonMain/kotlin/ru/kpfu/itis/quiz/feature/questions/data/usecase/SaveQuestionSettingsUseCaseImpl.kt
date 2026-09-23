package ru.kpfu.itis.quiz.feature.questions.data.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale
import ru.kpfu.itis.quiz.feature.questions.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.quiz.feature.questions.domain.usecase.SaveQuestionSettingsUseCase

class SaveQuestionSettingsUseCaseImpl(
    private val questionSettingsRepository: QuestionSettingsRepository,
) : SaveQuestionSettingsUseCase {
    override suspend fun invoke(
        difficulty: Difficulty?,
        category: Category?,
        gameMode: GameMode?,
        locale: AppLocale?,
    ) {
        withContext(Dispatchers.IO) {
            difficulty?.let {
                questionSettingsRepository.saveDifficulty(it)
            }
            category?.let {
                questionSettingsRepository.saveCategory(it)
            }
            gameMode?.let {
                questionSettingsRepository.saveGameMode(it)
            }
            locale?.let {
                questionSettingsRepository.saveLocale(it)
            }
        }
    }
}
