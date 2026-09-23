package ru.kpfu.itis.quiz.android.feature.questions.presentation.settings.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.kpfu.itis.quiz.core.util.normalizeEnumName
import ru.kpfu.itis.quiz.android.R
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi.QuestionSettingsScreenState
import ru.kpfu.itis.quiz.android.core.designsystem.components.DropdownMenu
import ru.kpfu.itis.quiz.android.core.designsystem.components.ErrorDialog
import ru.kpfu.itis.quiz.android.core.designsystem.components.TextButton
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.AppLocale
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.model.QuestionSettings
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi.QuestionSettingsScreenIntent
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.mvi.QuestionSettingsScreenSideEffect
import ru.kpfu.itis.quiz.feature.questions.presentation.settings.viewmodel.QuestionSettingsViewModel

@Composable
fun QuestionSettingsScreen(
    viewModel: QuestionSettingsViewModel = koinViewModel()
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    var error by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.onIntent(QuestionSettingsScreenIntent.GetQuestionSettings)

        effect.collect {
            when(it) {
                is QuestionSettingsScreenSideEffect.ShowError -> {
                    val errorMessage = it.message
                    val errorTitle = it.title

                    error = errorTitle to errorMessage
                }
            }
        }
    }

    ScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state.value,
        onCategoryChosen = { viewModel.onIntent(QuestionSettingsScreenIntent.UpdateCategory(it)) },
        onDifficultyChosen = { viewModel.onIntent(QuestionSettingsScreenIntent.UpdateDifficulty(it)) },
        onGameModeChosen = { viewModel.onIntent(QuestionSettingsScreenIntent.UpdateGameMode(it)) },
        onLocaleChosen = { viewModel.onIntent(QuestionSettingsScreenIntent.UpdateLocale(it)) },
        onSaveSettingsClick = { viewModel.onIntent(QuestionSettingsScreenIntent.SaveQuestionSettings) },
    )

    Box {
        error?.let {
            ErrorDialog(
                onDismiss = { error = null },
                title = it.first,
                text = it.second
            )
        }
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    state: QuestionSettingsScreenState,
    onCategoryChosen: (String) -> Unit,
    onDifficultyChosen: (String) -> Unit,
    onGameModeChosen: (String) -> Unit,
    onLocaleChosen: (AppLocale) -> Unit,
    onSaveSettingsClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Settings(
            settings = state.settings,
            onCategoryChosen = onCategoryChosen,
            onDifficultyChosen = onDifficultyChosen,
            onGameModeChosen = onGameModeChosen,
            onSaveClick = onSaveSettingsClick,
            onLocaleChosen = onLocaleChosen,
        )
    }
}

@Composable
fun Settings(
    settings: QuestionSettings?,
    onCategoryChosen: (String) -> Unit,
    onDifficultyChosen: (String) -> Unit,
    onGameModeChosen: (String) -> Unit,
    onLocaleChosen: (AppLocale) -> Unit,
    onSaveClick: () -> Unit
) {
    var localeChanged by remember { mutableStateOf(false) }
    val activity = LocalActivity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        DropdownMenu(
            value = stringArrayResource(R.array.categories)[
                settings?.category?.let { getStringArrayIndexByCategory(it) } ?: 0],
            suggestions = stringArrayResource(R.array.categories).toList(),
            label = stringResource(R.string.category),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = { onCategoryChosen(getCategoryByStringArrayIndex(it)) }
        )

        DropdownMenu(
            value = stringArrayResource(R.array.difficulties)[
                settings?.difficulty?.let { getStringArrayIndexByDifficulty(it) } ?: 0],
            suggestions = stringArrayResource(R.array.difficulties).toList(),
            label = stringResource(R.string.difficulty),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = { onDifficultyChosen(getDifficultyByStringArrayIndex(it)) }
        )

        DropdownMenu(
            value = stringArrayResource(R.array.game_modes)[
                settings?.gameMode?.let { getStringArrayIndexByGameMode(it) } ?: 0],
            suggestions = stringArrayResource(R.array.game_modes).toList(),
            label = stringResource(R.string.game_mode),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = { onGameModeChosen(getGameModeByStringArrayIndex(it)) }
        )

        DropdownMenu(
            value = stringArrayResource(R.array.locales)[
                settings?.locale?.let { getStringArrayIndexByLocale(it) } ?: 0],
            suggestions = stringArrayResource(R.array.locales).toList(),
            label = stringResource(R.string.locale),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = {
                onLocaleChosen(getLocaleByStringArrayIndex(it))
                localeChanged = true
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .padding(horizontal = 64.dp),
            onClick = {
                onSaveClick()
                if (localeChanged)
                    activity?.recreate()
            },
            text = stringResource(R.string.save),
        )
    }
}

fun getCategoryByStringArrayIndex(index: Int): String {
    return when(index) {
        1 -> Category.BOOK.toString().normalizeEnumName()
        2 -> Category.FILM.toString().normalizeEnumName()
        3 -> Category.MUSIC.toString().normalizeEnumName()
        4 -> Category.TV.toString().normalizeEnumName()
        5 -> Category.VIDEO_GAMES.toString().normalizeEnumName()
        6 -> Category.SPORTS.toString().normalizeEnumName()
        7 -> Category.GEOGRAPHY.toString().normalizeEnumName()
        8 -> Category.HISTORY.toString().normalizeEnumName()
        9 -> Category.ANIMALS.toString().normalizeEnumName()
        else -> Category.GENERAL.toString().normalizeEnumName()
    }
}

fun getStringArrayIndexByCategory(category: Category): Int {
    return when(category) {
        Category.GENERAL -> 0
        Category.BOOK -> 1
        Category.FILM -> 2
        Category.MUSIC -> 3
        Category.TV -> 4
        Category.VIDEO_GAMES -> 5
        Category.SPORTS -> 6
        Category.GEOGRAPHY -> 7
        Category.HISTORY -> 8
        Category.ANIMALS -> 9
    }
}

fun getDifficultyByStringArrayIndex(index: Int): String {
    return when(index) {
        0 -> Difficulty.EASY.toString().normalizeEnumName()
        2 -> Difficulty.HARD.toString().normalizeEnumName()
        else -> Difficulty.MEDIUM.toString().normalizeEnumName()
    }
}

fun getStringArrayIndexByDifficulty(difficulty: Difficulty): Int {
    return when(difficulty) {
        Difficulty.EASY -> 0
        Difficulty.MEDIUM -> 1
        Difficulty.HARD -> 2
    }
}

fun getGameModeByStringArrayIndex(index: Int): String {
    return when(index) {
        1 -> GameMode.CHALLENGE.toString().normalizeEnumName()
        2 -> GameMode.EXPERT.toString().normalizeEnumName()
        else -> GameMode.BLITZ.toString().normalizeEnumName()
    }
}

fun getStringArrayIndexByGameMode(gameMode: GameMode): Int {
    return when(gameMode) {
        GameMode.BLITZ -> 0
        GameMode.CHALLENGE -> 1
        GameMode.EXPERT -> 2
    }
}

fun getLocaleByStringArrayIndex(index: Int): AppLocale {
    return when(index) {
        1 -> AppLocale.EN_GB
        2 -> AppLocale.EN_US
        3 -> AppLocale.RU
        else -> AppLocale.SYSTEM
    }
}

fun getStringArrayIndexByLocale(locale: AppLocale): Int {
    return when(locale) {
        AppLocale.SYSTEM -> 0
        AppLocale.EN_GB -> 1
        AppLocale.EN_US -> 2
        AppLocale.RU -> 3
    }
}
