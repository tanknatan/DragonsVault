package com.dragons.vault.keys.chests.presentation.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dragons.vault.keys.chests.R
import com.dragons.vault.keys.chests.ui.theme.myfont
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dragons.vault.keys.chests.data.SoundManager
import com.dragons.vault.keys.chests.domain.LevelManager
import com.dragons.vault.keys.chests.presentation.navigation.Screen
import com.dragons.vault.keys.chests.presentation.navigation.navigatePopUpInclusive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MatchingGame(
    level: Int,
    onBack: (Screen) -> Unit,
    restartGame: () -> Unit,
    onNextLevel: () -> Unit,
) {
    val chests = listOf("chest_1", "chest_2", "chest_3", "chest_4", "chest_5", "chest_6")
    var shuffledKeys by remember {
        mutableStateOf(
            listOf(
                "key_1",
                "key_2",
                "key_3",
                "key_4",
                "key_5",
                "key_6"
            ).shuffled()
        )
    }
    var openedChests by remember { mutableStateOf(listOf<String>()) }
    var timerExpired by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(30) } // Начальное значение в секундах
    var gameWon by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }
    var isSettindsOpen by remember { mutableStateOf(false) }
    // Переменные для бонусов
    var stopTimerBonus by remember { mutableStateOf(5) } // Начальное количество использований бонуса для таймера
    var autoPlaceKeyBonus by remember { mutableStateOf(5) } // Начальное количество использований бонуса для ключей
    val minutes = (remainingTime / 60).toString().padStart(2, '0')
    val seconds = (remainingTime % 60).toString().padStart(2, '0')
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LevelPreferences", Context.MODE_PRIVATE)
    val levelManager = remember { LevelManager(sharedPreferences) }

    // Основная логика таймера
    LaunchedEffect(Unit) {
        while (remainingTime > 0 && !gameWon && !gameOver) {
            delay(1000)
            remainingTime--
        }
        if (!gameWon && remainingTime <= 0) {
            gameOver = true

        }
    }

    // Проверка на победу
    LaunchedEffect(openedChests) {
        if (openedChests.size == chests.size) {
            gameWon = true
            saveGameProgress(context, level, remainingTime)

        }
    }
    if (isSettindsOpen) {
        OptionsScreen(
            onBack = {
                isSettindsOpen = false
            })
        BackHandler {
            isSettindsOpen = false
        }
    } else {
        when {
            gameWon -> {
                WinScreen(onNextLevel, level, onBack,levelManager)
            }

            timerExpired || gameOver -> {
                LossScreen(restartGame, level, onBack)
            }

            else ->Box(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painter = painterResource(id = R.drawable.background),
                        contentScale = ContentScale.Crop
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {


                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        // Верхний ряд с двумя кнопками
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween, // Распределение кнопок по краям
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Кнопка "Назад" (слева)
                            Image(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        SoundManager.playSound()
                                        onBack(Screen.MainMenuScreen)
                                    }
                            )
                            Text(
                                "LEVEL $level",
                                fontSize = 35.sp,
                                fontFamily = myfont,
                                color = Color.White
                            )
                            // Кнопка справа
                            Image(
                                painter = painterResource(id = R.drawable.settings_icon), // Предполагаемая иконка настроек
                                contentDescription = "Settings",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        SoundManager.playSound()
                                        isSettindsOpen = true
                                        // Добавьте функцию для обработки нажатия
                                    }
                            )
                        }

                        // Остальная часть экрана (игровой контент)
                        // Здесь ваш основной контент экрана игры
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Заголовок "TIME"
                        Text(
                            text = "TIME",
                            fontSize = 14.sp, // Размер заголовка
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        // Отображение времени в формате MM:SS
                        Text(
                            text = "$minutes:$seconds", // Формат времени
                            fontSize = 36.sp, // Размер текста для времени
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Сетка сундуков 3x3
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (row in 0..1) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (col in 0..2) {
                                    val index = row * 3 + col
                                    Chest(
                                        name = chests[index],
                                        isOpened = openedChests.contains(chests[index]),
                                        onChestOpened = { key ->
                                            if (chests[index].endsWith(key.last().toString())) {
                                                openedChests = openedChests + chests[index]
                                                shuffledKeys = shuffledKeys - key
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Отображение перетаскиваемых ключей
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Первая строка ключей
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            shuffledKeys.take(3).forEach { key ->
                                DraggableKey(
                                    openedChests,
                                    name = key,
                                    onDragEnded = { position ->
                                        val matchedChest =
                                            chests.find { it.endsWith(key.last().toString()) }
                                        if (matchedChest != null && !openedChests.contains(
                                                matchedChest
                                            )
                                        ) {
                                            openedChests = openedChests + matchedChest
                                        }
                                    })
                            }
                        }

                        // Вторая строка ключей (смещенная)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp) // Смещение для шахматного порядка
                        ) {
                            shuffledKeys.drop(3).take(3).forEach { key ->
                                DraggableKey(
                                    openedChests,
                                    name = key,
                                    onDragEnded = { position ->
                                        val matchedChest =
                                            chests.find { it.endsWith(key.last().toString()) }
                                        if (matchedChest != null && !openedChests.contains(
                                                matchedChest
                                            )
                                        ) {
                                            openedChests = openedChests + matchedChest
                                        }
                                    })
                            }
                        }

                        // Третья строка ключей
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            shuffledKeys.drop(6).take(3).forEach { key ->
                                DraggableKey(
                                    openedChests,
                                    name = key,
                                    onDragEnded = { position ->
                                        val matchedChest =
                                            chests.find { it.endsWith(key.last().toString()) }
                                        if (matchedChest != null && !openedChests.contains(
                                                matchedChest
                                            )
                                        ) {
                                            openedChests = openedChests + matchedChest
                                        }
                                    })
                            }
                        }

                        // Четвертая строка ключей (смещенная)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp) // Смещение для шахматного порядка
                        ) {
                            shuffledKeys.drop(9).take(3).forEach { key ->
                                DraggableKey(
                                    openedChests,
                                    name = key,
                                    onDragEnded = { position ->
                                        val matchedChest =
                                            chests.find { it.endsWith(key.last().toString()) }
                                        if (matchedChest != null && !openedChests.contains(
                                                matchedChest
                                            )
                                        ) {
                                            openedChests = openedChests + matchedChest
                                        }
                                    })
                            }
                        }
                    }


                }

                // Бонусы внизу экрана
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Размещаем Row внизу экрана
                        .padding(bottom = 20.dp) // Можно добавить отступ снизу для удобства
                ) {
                    // Бонус: добавление 5 секунд к таймеру
                    TextButton(onClick = {
                        if (stopTimerBonus > 0) {
                            stopTimerBonus--
                            remainingTime += 5 // Добавляем 5 секунд к оставшемуся времени
                        }
                    }) {
                        Column(

                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Окружение бонуса кругом
                            Box(
                                contentAlignment = Alignment.Center, // Иконка будет по центру круга

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.timer_icon), // Бонус иконка таймера
                                    contentDescription = "Add 5 Seconds",
                                    modifier = Modifier.size(50.dp) // Задаем размер иконки
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.round), // Фон круга
                                    contentDescription = "Circle Background",
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Crop
                                )

                            }
                            Text(
                                "$stopTimerBonus/5", // Отображаем текущее количество бонусов
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Заголовок "TIME"
                        Text(
                            text = "chest opened",
                            fontSize = 14.sp, // Размер заголовка
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        // Отображение времени в формате MM:SS
                        Text(
                            text = "${openedChests.size}/${chests.size}", // Формат времени
                            fontSize = 36.sp, // Размер текста для времени
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // Показ количества открытых сундуков


                    // Бонус: автоматическое размещение одного ключа
                    TextButton(onClick = {
                        if (autoPlaceKeyBonus > 0 && shuffledKeys.isNotEmpty()) {
                            val nextKey = shuffledKeys.first()
                            val matchedChest =
                                chests.find { it.endsWith(nextKey.last().toString()) }
                            if (matchedChest != null) {
                                openedChests = openedChests + matchedChest
                                shuffledKeys = shuffledKeys - nextKey
                                autoPlaceKeyBonus--
                            }
                        }
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Окружение бонуса кругом
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.round), // Фон круга
                                    contentDescription = "Circle Background",
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.lightning_icon), // Бонус иконка ключа
                                    contentDescription = "Auto Place Key",
                                    modifier = Modifier.size(50.dp) // Задаем размер иконки
                                )
                            }
                            Text(
                                "$autoPlaceKeyBonus/5", // Отображаем текущее количество бонусов
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }


                }
            }
        }


    }


}


fun saveGameProgress(context: Context, level: Int, remainingTime: Int) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LevelPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val currentTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(currentTime))

    val stars = when {
        remainingTime > 20 -> 3
        remainingTime > 10 -> 2
        else -> 1
    }

    // Сохраняем данные о текущем уровне
    editor.putBoolean("level_${level}_completed", true)
    editor.putString("level_${level}_completion_date", formattedDate)
    editor.putInt("level_${level}_completion_time", 30 - remainingTime)
    editor.putInt("level_${level}_stars", stars)

    // Разблокируем следующий уровень, если это не последний уровень
    val nextLevel = level + 1
    if (nextLevel <= 9) {  // Предположим, что у нас всего 9 уровней
        editor.putBoolean("level_${nextLevel}_unlocked", true)
    }

    editor.apply()
}


@Composable
fun Chest(name: String, isOpened: Boolean, onChestOpened: (String) -> Unit) {
    val imageRes = when (name) {
        "chest_1" -> R.drawable.chest_1
        "chest_2" -> R.drawable.chest_2
        "chest_3" -> R.drawable.chest_3
        "chest_4" -> R.drawable.chest_4
        "chest_5" -> R.drawable.chest_5
        "chest_6" -> R.drawable.chest_6
        else -> R.drawable.chest_1 // Default case
    }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .then(
                    if (isOpened) Modifier.border(2.dp, Color.Green) else Modifier
                )
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        // Handle drag to open chest
                        if (dragAmount.y > 0f) {
                            onChestOpened(name)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isOpened) {
                Image(
                    painter = when (name) {
                        "chest_1" -> painterResource(id = R.drawable.key_1)
                        "chest_2" -> painterResource(id = R.drawable.key_2)
                        "chest_3" -> painterResource(id = R.drawable.key_3)
                        "chest_4" -> painterResource(id = R.drawable.key_4)
                        "chest_5" -> painterResource(id = R.drawable.key_5)
                        "chest_6" -> painterResource(id = R.drawable.key_6)
                        else -> painterResource(id = R.drawable.key_1) // Default case
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
fun DraggableKey(openedChests: List<String>, name: String, onDragEnded: (Offset) -> Unit) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val imageRes = when (name) {
        "key_1" -> R.drawable.key_1
        "key_2" -> R.drawable.key_2
        "key_3" -> R.drawable.key_3
        "key_4" -> R.drawable.key_4
        "key_5" -> R.drawable.key_5
        "key_6" -> R.drawable.key_6
        else -> R.drawable.key_1 // Default case
    }
    if (!openedChests.map { it.replace("chest", "key") }.contains(name)) {
        Box(
            modifier = Modifier
                .offset(x = offsetX.dp, y = offsetY.dp)
                .size(64.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            onDragEnded(Offset(offsetX, offsetY))
                        },
                        onDrag = { change, dragAmount ->
                            offsetX += (dragAmount.x / 1.9).roundToInt()
                            offsetY += (dragAmount.y / 1.9).roundToInt()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

