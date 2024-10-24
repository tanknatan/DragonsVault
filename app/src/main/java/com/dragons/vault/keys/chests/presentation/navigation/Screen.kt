package com.dragons.vault.keys.chests.presentation.navigation

import androidx.navigation.NavBackStackEntry
import com.arcade.galactic.racer.domain.Level


sealed class Screen(
    val screenRoute: String,
) {
    open val route: String = screenRoute

    data object SplashScreen : Screen("splash_screen")
    data object MainMenuScreen : Screen("main_menu_screen")
    data object GameScreen : Screen("game_screen") {
        override val route = "$screenRoute/{level}"
        fun getLevel(navBackStackEntry: NavBackStackEntry): Level {
            return navBackStackEntry.arguments?.getString("level")
                ?.let { Level.valueOf(it) } ?: Level.LEVEL_1
        }
    }

    data object StatisticsScreen : Screen("statistics_screen")
    data object OptionScreen : Screen("settings_screen")
    data object LevelScreen : Screen("level_screen")
    data object GameEndScreen : Screen("game_end_screen/{level}/{isVictory}")
    data object ExitScreen : Screen("exit_screen")
}