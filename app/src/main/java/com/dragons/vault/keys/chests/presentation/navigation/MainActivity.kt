package com.dragons.vault.keys.chests.presentation.navigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dragons.vault.keys.chests.data.Prefs
import com.dragons.vault.keys.chests.data.SoundManager
import com.dragons.vault.keys.chests.presentation.view.ExitScreen
import com.dragons.vault.keys.chests.presentation.view.LevelScreen
import com.dragons.vault.keys.chests.presentation.view.LossScreen

import com.dragons.vault.keys.chests.presentation.view.MainMenuScreen
import com.dragons.vault.keys.chests.presentation.view.MatchingGame
import com.dragons.vault.keys.chests.presentation.view.OptionsScreen
import com.dragons.vault.keys.chests.presentation.view.SplashScreen
import com.dragons.vault.keys.chests.presentation.view.StatisticsScreen
import com.dragons.vault.keys.chests.presentation.view.WinScreen
import com.dragons.vault.keys.chests.ui.theme.DragonsVaultTheme
import com.dragons.vault.keys.chests.ui.theme.myfont

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        Prefs.init(applicationContext)
        SoundManager.init(applicationContext)
        SoundManager.playMusic()
        setContent {
            DragonsVaultTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                ) {

                    composable(route = Screen.SplashScreen.route) {
                        SplashScreen(navController::navigatePopUpInclusive)
                    }
                    composable(Screen.MainMenuScreen.route) {
                        MainMenuScreen(navController::navigatePopUpInclusive)
                    }
                    composable(Screen.LevelScreen.route) {
                        LevelScreen(
                            onBack = navController::navigatePopUpInclusive ,
                            onChooseLevel = { level ->
                                navController.navigate(Screen.GameScreen.routeWithArgs(level))
                            }
                        )
                    }
                    composable(Screen.GameScreen.route, arguments = listOf(navArgument("level") { type = NavType.IntType })) { backStackEntry ->
                        val level = backStackEntry.arguments?.getInt("level") ?: 1
                        MatchingGame(level = level, navController::navigatePopUpInclusive,
// Функция для перехода на следующий уровень
                            onNextLevel = {
                                // Переход на следующий уровень
                                navController.navigate("game_screen/${level + 1}") {
                                    // Очищаем стек до текущего уровня
                                    popUpTo("game_screen/$level") { inclusive = true }
                                }
                            }, restartGame = {
                                // Перезапуск текущего уровня
                                navController.navigate("game_screen/$level") {
                                    // Очищаем стек до текущего уровня
                                    popUpTo("game_screen/$level") { inclusive = true }
                                }
                            }

                            ,)
                    }

                    composable(Screen.ExitScreen.route) {
                        ExitScreen(
                            navController::navigatePopUpInclusive
                        )
                    }
                    composable(Screen.OptionScreen.route) {
                        OptionsScreen(navController::navigatePopUpInclusive)
                    }
                    composable(Screen.StatisticsScreen.route) {
                        StatisticsScreen(
                            navController::navigatePopUpInclusive
                        )
                    }

                    composable("matching_game/{level}", arguments = listOf(navArgument("level") { type = NavType.IntType })) { backStackEntry ->
                        val level = backStackEntry.arguments?.getInt("level") ?: 1
                        MatchingGame(level = level,  navController::navigatePopUpInclusive,onNextLevel = {
                            // Переход на следующий уровень
                            navController.navigate("game_screen/${level + 1}") {
                                // Очищаем стек до текущего уровня
                                popUpTo("game_screen/$level") { inclusive = true }
                            }
                        }, restartGame = {
                            // Перезапуск текущего уровня
                            navController.navigate("game_screen/$level") {
                                // Очищаем стек до текущего уровня
                                popUpTo("game_screen/$level") { inclusive = true }
                            }
                        },)
                    }


                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        SoundManager.resumeSound()
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseSound()
        SoundManager.pauseMusic()
        SoundManager.pauseGameMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.onDestroy()
    }
}





@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fillColor: Color = Color.Unspecified,
    outlineColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    outlineDrawStyle: Stroke = Stroke(
        width = 20f,
    ),
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = Modifier.semantics { invisibleToUser() },
            color = outlineColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = myfont,
            letterSpacing = letterSpacing,
            textDecoration = null,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style.copy(
                shadow = null,
                drawStyle = outlineDrawStyle,
            ),
        )

        Text(
            text = text,
            color = fillColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = myfont,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
}