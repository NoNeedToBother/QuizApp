package ru.kpfu.itis.quiz.android.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import ru.kpfu.itis.quiz.android.R
import ru.kpfu.itis.quiz.android.core.designsystem.theme.AppTheme
import ru.kpfu.itis.quiz.android.feature.auth.presentation.login.SignInScreen
import ru.kpfu.itis.quiz.android.feature.auth.presentation.registration.RegisterScreen
import ru.kpfu.itis.quiz.android.core.navigation.Routes
import ru.kpfu.itis.quiz.android.feature.leaderboard.presentation.ui.screens.LeaderboardsScreen
import ru.kpfu.itis.quiz.android.feature.profile.presentation.ui.screens.OtherUserProfileScreen
import ru.kpfu.itis.quiz.android.feature.profile.presentation.ui.screens.ProfileScreen
import ru.kpfu.itis.quiz.android.feature.questions.presentation.questions.ui.screens.QuestionsScreen
import ru.kpfu.itis.quiz.android.feature.questions.presentation.settings.ui.screens.QuestionSettingsScreen
import ru.kpfu.itis.quiz.android.feature.users.presentation.ui.screens.SearchUsersScreen
import ru.kpfu.itis.quiz.core.util.AppLocale
import ru.kpfu.itis.quiz.core.util.currentLocale
import ru.kpfu.itis.quiz.feature.questions.domain.repository.QuestionSettingsRepository
import java.util.Locale

data class TopLevelRoute(val name: String, val route: String, val icon: ImageVector)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                val topLevelRoutes = listOf(
                    TopLevelRoute(
                        stringResource(R.string.search_users), Routes.SearchUsersScreen.route,
                        ImageVector.vectorResource(R.drawable.search_users)
                    ),
                    TopLevelRoute(
                        stringResource(R.string.leaderboards), Routes.LeaderboardsScreen.route,
                        ImageVector.vectorResource(R.drawable.leaderboard)
                    ),
                    TopLevelRoute(
                        stringResource(R.string.main_menu), Routes.MainMenuScreen.route,
                        ImageVector.vectorResource(R.drawable.main_menu)
                    ),
                    TopLevelRoute(
                        stringResource(R.string.profile), Routes.ProfileScreen.route,
                        ImageVector.vectorResource(R.drawable.profile)
                    ),
                )

                MainScreen(
                    topLevelRoutes = topLevelRoutes,
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val repository = get<QuestionSettingsRepository>()

        val locale = runBlocking { repository.getLocale() }
        currentLocale = locale
        val javaLocale = when (locale) {
            AppLocale.SYSTEM -> Locale.getDefault()
            AppLocale.EN_GB -> Locale("en", "GB")
            AppLocale.EN_US -> Locale("en", "US")
            AppLocale.RU -> Locale("ru", "RU")
        }

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(javaLocale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

}

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    topLevelRoutes: List<TopLevelRoute>,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = navController,
                topLevelRoutes = topLevelRoutes,
            )
        }
    ) { padding ->
        NavHost(navController, startDestination = Routes.SignInScreen.route, modifier = Modifier.padding(padding)) {
            composable(Routes.SignInScreen.route) { SignInScreen(
                goToRegisterScreen = {
                    navController.navigate(Routes.RegisterScreen.route)
                },
                goToMainMenuScreen = {
                    navController.clearBackStack(Routes.SignInScreen.route)
                    navController.clearBackStack(Routes.RegisterScreen.route)
                    navController.navigate(Routes.MainMenuScreen.route)
                }
            ) }

            composable(Routes.RegisterScreen.route) { RegisterScreen(
                goToSignInScreen = {
                    navController.navigate(Routes.SignInScreen.route)
                },
                goToMainMenuScreen = {
                    navController.clearBackStack(Routes.SignInScreen.route)
                    navController.clearBackStack(Routes.RegisterScreen.route)
                    navController.navigate(Routes.MainMenuScreen.route)
                }
            ) }

            composable(Routes.MainMenuScreen.route) { MainMenuScreen(
                goToQuestionsScreen = {
                    navController.navigate(Routes.QuestionsScreen.route)
                },
                goToQuestionSettingsScreen = {
                    navController.navigate(Routes.QuestionSettingsScreen.route)
                },
            ) }

            composable(Routes.QuestionsScreen.route) { QuestionsScreen() }
            composable(Routes.QuestionSettingsScreen.route) { QuestionSettingsScreen() }

            composable(Routes.ProfileScreen.route) { ProfileScreen(
                goToSignInScreen = {
                    navController.navigate(Routes.SignInScreen.route)
                }
            ) }

            composable(
                route = Routes.UserScreen.route + "/{userId}"
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.toLong()
                userId?.let { OtherUserProfileScreen(userId = it) }
            }
            composable(Routes.LeaderboardsScreen.route) { LeaderboardsScreen(
                goToUserScreen = { id ->
                    navController.navigate(Routes.UserScreen.route + "/$id")
                }
            ) }
            composable(Routes.SearchUsersScreen.route) { SearchUsersScreen(
                goToUserScreen = { id ->
                    navController.navigate(Routes.UserScreen.route + "/$id")
                }
            ) }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigation(
    navController: NavController,
    topLevelRoutes: List<TopLevelRoute>,
) {
    var showNavBar by remember { mutableStateOf(false) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collectLatest { backStackEntry ->
            val currentRoute = backStackEntry.destination.route
            showNavBar = when (currentRoute) {
                Routes.SignInScreen.route, Routes.RegisterScreen.route -> false
                else -> true
            }
        }
    }

    AnimatedVisibility(
        visible = showNavBar,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 750))
    ) {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            topLevelRoutes.forEach { topLevelRoute ->
                NavigationBarItem(
                    icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                    label = { Text(topLevelRoute.name, textAlign = TextAlign.Center) },
                    selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(
                            topLevelRoute.route,
                            null
                        )
                    } == true,
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            navController.graph.findStartDestination().route?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
