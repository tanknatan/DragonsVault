package com.dragons.vault.keys.chests.presentation.view
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
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
fun LossScreen(restart: () -> Unit, level: Int, onBack: (Screen) -> Unit) {
    LaunchedEffect(Unit) {
        SoundManager.pauseGameMusic()
        SoundManager.resumeMusic()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background), // Background image
                contentScale = ContentScale.FillBounds
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Добавляем изображение "LOSE" сверху
            Image(
                painter = painterResource(id = R.drawable.lose), // Ваше изображение LOSE
                contentDescription = "LOSE",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(100.dp) // Задаем фиксированную высоту
            )

            // Добавляем текст с номером уровня
            Text(
                text = "LEVEL $level",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Отображение звезд
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Ensure stars fill about 50% width
            ) {
                // Отображаем неактивные звезды
                repeat(3) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star_empty), // Пустая звезда
                        contentDescription = "Star",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(50.dp) // Размер звезды
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Текст "TIME IS OVER"
            Text(
                text = "TIME IS OVER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Repeat Button (Повторить)
            Box(
                modifier = Modifier.padding(vertical = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bbutton), // Button background
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clickable {
                            SoundManager.playSound()
                            restart()
                        }
                )
                Text(
                    text = "REPEAT",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Home Button (Домой)
            Box(
                modifier = Modifier.padding(vertical = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button), // Button background
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clickable {
                            SoundManager.playSound()
                            onBack(Screen.LevelScreen)
                        }
                )
                Text(
                    text = "HOME",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Preview
@Composable
fun LossScreenPreview() {
    LossScreen({}, 1, {})
}


