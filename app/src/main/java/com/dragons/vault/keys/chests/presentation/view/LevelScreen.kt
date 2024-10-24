package com.dragons.vault.keys.chests.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Savage.Trail.presentation.navigatio.Screen
import com.arcade.galactic.racer.domain.Level
import com.dragons.vault.keys.chests.R

import com.dragons.vault.keys.chests.data.SoundManager
import com.dragons.vault.keys.chests.domain.LevelManager
import com.dragons.vault.keys.chests.presentation.navigation.OutlinedText


@Composable
fun LevelScreen(
    onBack: (Screen) -> Unit,
    onChooseLevel: (Level) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LevelPreferences", Context.MODE_PRIVATE)

    // Используем LevelManager с sharedPreferences
    val levelManager = remember { LevelManager(sharedPreferences) }
    BackHandler {
        onBack(Screen.MainMenuScreen)
    }
    LaunchedEffect(Unit) {
        SoundManager.pauseGameMusic()
        SoundManager.resumeMusic()
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.background), // Замените на ваше фоновое изображение
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Верхняя панель с кнопкой назад
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
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
                Spacer(modifier = Modifier.width(70.dp))
                OutlinedText(
                    text = "Levels",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Отображение 9 плашек уровней
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (level in 1..9) { // 9 уровней
                    val isUnlocked = levelManager.isLevelUnlocked(level)
                    val stars = levelManager.getStarsForLevel(level)  // Получаем количество звёзд из LevelManager

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()  // Плашка на всю ширину
                            .padding(vertical = 0.dp)  // Отступы между плашками
                            .clickable(enabled = isUnlocked) {
                                if (isUnlocked) {
                                    SoundManager.playSound()
                                    onChooseLevel(Level.entries[level - 1])
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        LevelBanner(level = level, stars = stars)  // Передаем количество звёзд в LevelBanner
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LevelScreenPreview() {
    LevelScreen(onBack = {}, onChooseLevel = {})
}

@Composable
fun LevelBanner(level: Int, stars: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)  // Уменьшенный отступ снаружи
            .clip(RoundedCornerShape(8.dp))  // Меньший радиус скругления
            .background(Color(0x99A52A2A))  // Полупрозрачный фон
            .border(2.dp, Color(0xFF7F00FF), RoundedCornerShape(8.dp)), // Фиолетовая рамка
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),  // Уменьшенные внутренние отступы
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Отображение текста уровня
            Text(
                text = "LEVEL $level",
                color = Color.White,
                fontSize = 25.sp,  // Меньший размер текста
                fontWeight = FontWeight.Bold
            )

            // Отображение звёзд
            Row {
                for (i in 1..3) {
                    Icon(
                        painter = painterResource(id = if (i <= stars) R.drawable.ic_star_filled else R.drawable.ic_star_empty),
                        contentDescription = "Star $i",
                        tint = if (i <= stars) Color.Yellow else Color.Black,
                        modifier = Modifier.size(35.dp)  // Уменьшенный размер звёзд
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Отступ между звёздами
                }
            }
        }
    }
}


