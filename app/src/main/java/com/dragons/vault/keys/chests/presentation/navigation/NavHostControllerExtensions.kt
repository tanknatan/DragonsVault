package com.dragons.vault.keys.chests.presentation.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.arcade.galactic.racer.domain.Level


fun NavHostController.navigateSingleTop(
    screen: Screen,
    vararg args: Any,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val routeBuilder = StringBuilder(screen.screenRoute)

    val appendArg: (Any) -> Unit = { arg: Any ->
        routeBuilder.append("/$arg")
    }

    args.forEach(appendArg)

    navigate(routeBuilder.toString()) {
        builder(this)
        launchSingleTop = true
    }
}

fun NavHostController.navigatePopUpInclusive(screen: Screen) {
    val currentRoute = currentBackStackEntry?.destination?.route ?: return
    navigate(screen.route) {
        popUpTo(currentRoute) {
            inclusive = true

        }
    }
}

fun NavHostController.navigateSingleTopp(
    screen: Screen,
    level: Level,
    isVictory: Boolean,
) {
    val route = "${screen.screenRoute}/${level.name}/$isVictory"
    navigate(route) {
        launchSingleTop = true
    }
}