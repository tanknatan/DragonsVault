package com.dragons.vault.keys.chests.presentation.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.Savage.Trail.presentation.navigatio.Screen

import com.dragons.vault.keys.chests.R
import com.dragons.vault.keys.chests.presentation.navigation.OutlinedText


import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onNext: (Screen) -> Unit) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000) // Short delay before transitioning
        isLoading = false
        onNext(Screen.MainMenuScreen)
    }



    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop, // To make the image fill the entire screen
            modifier = Modifier.fillMaxSize()
        )

        // Column removed from center and placed at bottom center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add padding if necessary

            contentAlignment = Alignment.BottomCenter // Changed to BottomCenter
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                RotatingLoadingImage()
            }

            OutlinedText(
                text = "Loading...",
                outlineColor = Color(0xFFFFFFFF),
                fillColor = Color.Black,
                fontSize = 50.sp,
            )
        }
    }
}

@Composable
fun RotatingLoadingImage() {
    // Создаем бесконечную анимацию для вращения
    val infiniteTransition = rememberInfiniteTransition()

    // Анимация для вращения изображения
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Отображение изображения с анимацией вращения
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.loading_image), // замените на свой ресурс изображения
            contentDescription = "Loading Image",
            modifier = Modifier
                .size(100.dp)    // Размер изображения
                .rotate(rotation)  // Применение анимации вращения
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen {}
}


