package com.dragons.vault.keys.chests.presentation.view

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dragons.vault.keys.chests.presentation.navigation.Screen
import com.dragons.vault.keys.chests.R

import com.dragons.vault.keys.chests.data.SoundManager
import com.dragons.vault.keys.chests.domain.LevelManager
import com.dragons.vault.keys.chests.presentation.navigation.OutlinedText

@Composable
fun StatisticsScreen(
    onBack: (Screen) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LevelPreferences", Context.MODE_PRIVATE)
    val levelManager = remember { LevelManager(sharedPreferences) }

    // Получаем список завершенных уровней
    val completedLevels = levelManager.getCompletedLevels()

    BackHandler {
        onBack(Screen.MainMenuScreen)
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
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
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
                    text = "Statistics",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Отображение статистики по уровням
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(completedLevels) { level ->
                    val date = levelManager.getCompletionDate(level) ?: "Not completed"
                    val time = levelManager.getCompletionTime(level) ?: "N/A"

                    LevelStatsRow(level = level, date = date, time = time)
                }
            }
        }
    }
}

@Composable
fun LevelStatsRow(level: Int, date: String, time: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x99A52A2A))
            .border(2.dp, Color(0xFF7F00FF), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Отображение номера уровня
            Text(
                text = "Level $level",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Отображение даты прохождения
            Text(
                text = date,
                color = Color.White,
                fontSize = 16.sp
            )

            // Отображение времени прохождения
            Text(
                text = time,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}


@Preview
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen(onBack = {})
}
