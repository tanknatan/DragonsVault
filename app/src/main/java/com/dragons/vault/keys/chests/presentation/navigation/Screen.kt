package com.dragons.vault.keys.chests.presentation.navigation

import androidx.navigation.NavBackStackEntry
import com.arcade.galactic.racer.domain.Level


sealed class Screen(
    val screenRoute: String,
) {
    open val route: String = screenRoute

    data object SplashScreen : Screen("splash_screen")
    data object MainMenuScreen : Screen("main_menu_screen")
    object LevelScreen : Screen("level_screen")
    object GameScreen : Screen("game_screen/{level}") { // Путь с аргументом
        fun routeWithArgs(level: Int) = "game_screen/$level" // Функция для создания маршрута с аргументом
    }

    data object StatisticsScreen : Screen("statistics_screen")
    data object OptionScreen : Screen("settings_screen")
    data object GameWonScreen : Screen("game_won_screen")
    data object GameLostScreen : Screen("game_lost_screen")
    data object ExitScreen : Screen("exit_screen")
}