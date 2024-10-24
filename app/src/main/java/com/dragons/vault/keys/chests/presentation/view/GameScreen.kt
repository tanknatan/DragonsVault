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
import kotlin.math.roundToInt

@Composable
fun MatchingGame() {
    val chests = listOf("chest_1", "chest_2", "chest_3", "chest_4", "chest_5", "chest_6")
    var shuffledKeys by remember { mutableStateOf(listOf("key_1", "key_2", "key_3", "key_4", "key_5", "key_6").shuffled()) }
    var openedChests by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background),
                contentScale = ContentScale.Crop
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Level 1", fontSize = 24.sp, fontFamily = myfont, color= Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        // Display chests in a 3x3 grid
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

        // Display draggable keys in random order
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            shuffledKeys.forEach { key ->
                DraggableKey(openedChests, name = key, onDragEnded = { position ->
                    // Match the key to the chest
                    val matchedChest = chests.find { it.endsWith(key.last().toString()) }
                    if (matchedChest != null && !openedChests.contains(matchedChest)) {
                        openedChests = openedChests + matchedChest
                    }
                })
            }
        }
    }
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
        modifier = Modifier ,
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
                }
            ,
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
