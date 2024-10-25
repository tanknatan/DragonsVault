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
fun WinScreen(next: () -> Unit, level: Int, onBack: (Screen) -> Unit, levelManager: LevelManager) {
    // Получаем количество звезд для уровня из SharedPreferences
    val stars = levelManager.getStarsForLevel(level)

    LaunchedEffect(Unit) {
        SoundManager.pauseGameMusic()
        SoundManager.resumeMusic()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background), // Background image
                contentScale = ContentScale.Crop
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Добавляем изображение "WIN" сверху
            Image(
                painter = painterResource(id = R.drawable.win), // Ваше изображение WIN
                contentDescription = "WIN",
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

            // Score Display Box with stars
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Ensure stars fill about 50% width
            ) {
                // Отображаем звезды в зависимости от их количества (0, 1, 2, 3)
                repeat(3) { index ->
                    val starImage = if (stars > index) {
                        painterResource(id = R.drawable.ic_star_filled) // Активная звезда
                    } else {
                        painterResource(id = R.drawable.ic_star_empty) // Неактивная звезда
                    }

                    Image(
                        painter = starImage,
                        contentDescription = "Star",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(50.dp) // Размер звезды
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Next Level Button
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
                            next()
                        }
                )
                Text(
                    text = "NEXT",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Home Button
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




