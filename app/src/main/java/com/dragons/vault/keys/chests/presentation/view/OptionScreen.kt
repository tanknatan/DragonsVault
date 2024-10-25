package com.dragons.vault.keys.chests.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dragons.vault.keys.chests.presentation.navigation.Screen

import com.dragons.vault.keys.chests.R
import com.dragons.vault.keys.chests.data.Prefs
import com.dragons.vault.keys.chests.data.SoundManager
import com.dragons.vault.keys.chests.presentation.navigation.OutlinedText


@Composable
fun OptionsScreen(onBack: (Screen) -> Unit) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var musicVolume by remember { mutableFloatStateOf(Prefs.music) }
    var soundVolume by remember { mutableFloatStateOf(Prefs.sound) }

    val gradient1 = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE1E644), Color(0xFFD5C059)) // черный и фиолетовый цвет
    )
    val gradient2 = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE6D644), Color(0xFFD5B659)) // черный и фиолетовый цвет
    )

    // Обновление значений громкости при изменении
    LaunchedEffect(musicVolume) {
        Prefs.music = musicVolume
        SoundManager.setVolumeMusic()
        SoundManager.setVolumeGameMusic()
    }

    LaunchedEffect(soundVolume) {
        Prefs.sound = soundVolume
        SoundManager.setVolumeSound()
    }
    LaunchedEffect(Unit) {
        SoundManager.pauseGameMusic()
        SoundManager.resumeMusic()
    }
    BackHandler {
        onBack(Screen.MainMenuScreen)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.background), // Замените на ваше фоновое изображение
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Row в верхней части экрана
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
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
            Spacer(modifier = Modifier.width(40.dp))

        }

        // Основной контейнер с фоном main_rec для заголовка и слайдеров
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 100.dp), // Сдвигаем вниз, чтобы оставить место для Row
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedText(
                text = "OPTIONS",
                outlineColor = Color.Black,
                fillColor = Color.White,
                fontSize = 30.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.main_rec), // Используйте ваше изображение main_rec
//                    contentDescription = null,
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(250.dp) // Уменьшаем высоту main_rec
//                )

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Заголовок "Settings" на фоне main_rec
                    Spacer(modifier = Modifier.height(5.dp))
                    // Sound Volume настройка с текстом слева от слайдера
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp, vertical = 8.dp)
                    ) {
                        OutlinedText(
                            text = "Sound",
                            outlineColor = Color.White,
                            fillColor = Color(0xFFE04874),
                            fontSize = 24.sp,
                            modifier = Modifier.weight(1f) // Равное распределение
                        )
                        Slider(
                            value = soundVolume,
                            onValueChange = { newValue ->
                                soundVolume = newValue
                                sharedPreferences.edit().putFloat("soundVolume", newValue).apply()
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.Transparent,
                                inactiveTrackColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(2f)
                                .drawBehind {
                                    val trackWidth = size.width
                                    val trackHeight = size.height / 4f
                                    val topLeft = Offset(0f, center.y - trackHeight / 2f)
                                    val size = Size(trackWidth, trackHeight)
                                    drawRoundRect(
                                        brush = gradient1,
                                        topLeft = topLeft,
                                        size = size,
                                        cornerRadius = CornerRadius(10f, 10f)
                                    )
                                },
                            valueRange = 0f..1f,
                            steps = 10
                        )
                    }

                    // Music Volume настройка с текстом слева от слайдера
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp, vertical = 8.dp)
                    ) {
                        OutlinedText(
                            text = "Music",
                            outlineColor = Color.White,
                            fillColor = Color(0xFFE04874),
                            fontSize = 24.sp,
                            modifier = Modifier.weight(1f) // Равное распределение
                        )
                        Slider(
                            value = musicVolume,
                            onValueChange = { newValue ->
                                musicVolume = newValue
                                sharedPreferences.edit().putFloat("musicVolume", newValue).apply()
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.Transparent,
                                inactiveTrackColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(2f)
                                .drawBehind {
                                    val trackWidth = size.width
                                    val trackHeight = size.height / 4f
                                    val topLeft = Offset(0f, center.y - trackHeight / 2f)
                                    val size = Size(trackWidth, trackHeight)
                                    drawRoundRect(
                                        brush = gradient2,
                                        topLeft = topLeft,
                                        size = size,
                                        cornerRadius = CornerRadius(10f, 10f)
                                    )
                                },
                            valueRange = 0f..1f,
                            steps = 10
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back Button
            Box(
                modifier = Modifier
                    .size(150.dp, 100.dp)
                    .clickable { onBack(Screen.MainMenuScreen) }
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button), // Фон для кнопки
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(59.dp)),
                    contentScale = ContentScale.FillWidth
                )
                OutlinedText(
                    text = "SAVE",
                    outlineColor = Color.Gray,
                    fillColor = Color.White,
                    fontSize = 20.sp // Немного уменьшенный размер текста
                )
            }
        }
    }

}
