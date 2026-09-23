package ru.kpfu.itis.quiz.android.feature.leaderboard.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.kpfu.itis.quiz.android.R
import ru.kpfu.itis.quiz.android.core.designsystem.components.DropdownMenu
import ru.kpfu.itis.quiz.android.core.designsystem.components.TextButton
import ru.kpfu.itis.quiz.core.model.Category
import ru.kpfu.itis.quiz.core.model.Difficulty
import ru.kpfu.itis.quiz.core.model.GameMode
import ru.kpfu.itis.quiz.core.util.normalizeEnumName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheetWrapper(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onDifficultyChosen: (String) -> Unit,
    onCategoryChosen: (String) -> Unit,
    onGameModeChosen: (String) -> Unit,
    difficultyValue: String,
    categoryValue: String,
    gameModeValue: String,
    content: @Composable (PaddingValues) -> Unit
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val coroutineScope = rememberCoroutineScope()

    val onExpandClick = {
        coroutineScope.launch {
            if (sheetState.currentValue == SheetValue.PartiallyExpanded) {
                sheetState.expand()
            } else {
                sheetState.partialExpand()
            }
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(
                onExpandClick = { onExpandClick() },
                onSaveClick = onSaveClick,
                onDifficultyChosen = onDifficultyChosen,
                onCategoryChosen = onCategoryChosen,
                onGameModeChosen = onGameModeChosen,
                difficultyValue = difficultyValue,
                categoryValue = categoryValue,
                gameModeValue = gameModeValue
            )
        },
        scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState),
        modifier = modifier.fillMaxWidth(),
        sheetContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        sheetDragHandle = {
            IconButton(onClick = { onExpandClick() }, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = if (sheetState.currentValue == SheetValue.PartiallyExpanded)
                        Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                    contentDescription = "Expand/Collapse"
                )
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun BottomSheetContent(
    difficultyValue: String,
    categoryValue: String,
    gameModeValue: String,
    onExpandClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDifficultyChosen: (String) -> Unit,
    onCategoryChosen: (String) -> Unit,
    onGameModeChosen: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        DropdownMenu(
            suggestions = stringArrayResource(R.array.leaderboard_difficulties).toList(),
            label = stringResource(R.string.difficulty),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = { onDifficultyChosen(getDifficultyByStringArrayIndex(it)) },
            value = difficultyValue
        )

        DropdownMenu(
            suggestions = stringArrayResource(R.array.game_modes).toList(),
            label = stringResource(R.string.game_mode),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = { onGameModeChosen(getGameModeByStringArrayIndex(it)) },
            value = gameModeValue
        )

        DropdownMenu(
            suggestions = stringArrayResource(R.array.leaderboard_categories).toList(),
            label = stringResource(R.string.category),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onChosen = { onCategoryChosen(getCategoryByStringArrayIndex(it)) },
            value = categoryValue
        )

        TextButton(
            onClick = {
                onExpandClick()
                onSaveClick()
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.save)
        )
    }
}

fun getCategoryByStringArrayIndex(index: Int): String {
    return when(index) {
        1 -> Category.GENERAL.toString().normalizeEnumName()
        2 -> Category.BOOK.toString().normalizeEnumName()
        3 -> Category.FILM.toString().normalizeEnumName()
        4 -> Category.MUSIC.toString().normalizeEnumName()
        5 -> Category.TV.toString().normalizeEnumName()
        6 -> Category.VIDEO_GAMES.toString().normalizeEnumName()
        7 -> Category.SPORTS.toString().normalizeEnumName()
        8 -> Category.GEOGRAPHY.toString().normalizeEnumName()
        9 -> Category.HISTORY.toString().normalizeEnumName()
        10 -> Category.ANIMALS.toString().normalizeEnumName()
        else -> "Any"
    }
}

fun getDifficultyByStringArrayIndex(index: Int): String {
    return when(index) {
        1 -> Difficulty.EASY.toString().normalizeEnumName()
        2 -> Difficulty.MEDIUM.toString().normalizeEnumName()
        3 -> Difficulty.HARD.toString().normalizeEnumName()
        else -> "Any"
    }
}

fun getGameModeByStringArrayIndex(index: Int): String {
    return when(index) {
        1 -> GameMode.CHALLENGE.toString().normalizeEnumName()
        2 -> GameMode.EXPERT.toString().normalizeEnumName()
        else -> GameMode.BLITZ.toString().normalizeEnumName()
    }
}
