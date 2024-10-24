package com.dragons.vault.keys.chests.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dragons.vault.keys.chests.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@Composable
fun GameScreen() {
    var keys by remember { mutableStateOf(generateKeys()) }
    val chests = generateChests()

    var selectedKeys by remember { mutableStateOf(emptyList<Key>()) }
    var matchedChests by remember { mutableStateOf(mutableSetOf<Int>()) }
    var blockUi by remember { mutableStateOf(false) }
    var trigger by remember { mutableStateOf(false) }
    var droppedIndexes by remember { mutableStateOf(emptyList<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Match Keys to Chests",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Create chests in two rows
                chests.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { chest ->
                            Chest(
                                chest = chest,
                                isMatched = matchedChests.contains(chest.id) // Проверяем, сопоставлен ли сундук
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Create keys in two rows
                keys.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { key ->
                            var offsetX by remember { mutableStateOf(0f) }
                            var offsetY by remember { mutableStateOf(0f) }
                            var lastOffset by remember { mutableStateOf(Offset.Zero) }
                            val dp45InPx = LocalDensity.current.run { 45.dp.toPx() }

                            Box(
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            offsetX.roundToInt(),
                                            offsetY.roundToInt()
                                        )
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            offsetX += dragAmount.x
                                            offsetY += dragAmount.y
                                            if (!blockUi) {
                                                val targetChest = chests.find { it.id == key.id } // Найдём сундук с таким же id, как у ключа
                                                targetChest?.let {
                                                    if (lastOffset.y in (0f - dp45InPx)..(0f + dp45InPx)) { // Условие для попадания в сундук
                                                        offsetX = 0f
                                                        offsetY = 0f
                                                        if (selectedKeys.find { it.id == key.id } == null) {
                                                            selectedKeys = selectedKeys + key.copy(id = key.id)
                                                            matchedChests.add(key.id) // Добавляем сундук в список сопоставленных
                                                        }
                                                        trigger = !trigger

                                                        keys.firstOrNull { it.id == key.id }?.let {
                                                            keys = keys - it
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    .onGloballyPositioned { coordinates ->
                                        lastOffset = coordinates.positionInRoot()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = key.name, color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Chest(chest: Chest, isMatched: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .background(if (isMatched) Color.Green else Color.Blue) // Подсвечиваем сундук зелёным, если ключ на месте
    ) {
        Text(text = chest.name, color = Color.White)
    }
}

data class Key(val id: Int, val name: String)
data class Chest(val id: Int, val name: String)

fun generateKeys(): List<Key> {
    return listOf(
        Key(1, "Key 1"),
        Key(2, "Key 2"),
        Key(3, "Key 3"),
        Key(4, "Key 4"),
        Key(5, "Key 5"),
        Key(6, "Key 6")
    )
}

fun generateChests(): List<Chest> {
    return listOf(
        Chest(1, "Chest 1"),
        Chest(2, "Chest 2"),
        Chest(3, "Chest 3"),
        Chest(4, "Chest 4"),
        Chest(5, "Chest 5"),
        Chest(6, "Chest 6")
    )
}
//@Composable
//fun GameScreen(onBack: (Screen) -> Unit) {
//    val chestKeys = remember { generateChestsAndKeys() }  // Генерация сундуков и ключей
//    val matchedKeys =
//        remember { mutableStateListOf<Pair<Int, Int>>() } // Хранение успешных матчей ключей и сундуков
//    val shuffledKeys = remember { chestKeys.shuffled() } // Случайный порядок ключей
//    var chestPositions = remember { mutableStateMapOf<Int, LayoutCoordinates>() }
//    var keyInitialPositions =
//        remember { mutableStateMapOf<Int, Offset>() } // Начальные позиции ключей
//    var keyCurrentPositions =
//        remember { mutableStateMapOf<Int, Offset>() } // Текущие позиции ключей
//    var isGameFinished by remember { mutableStateOf(false) }
//
//    // Проверяем, все ли ключи сопоставлены. Если да, то завершаем игру
//    if (matchedKeys.size == chestKeys.size && !isGameFinished) {
//        isGameFinished = true
//        onBack(Screen.MainMenuScreen)  // Вызов функции окончания игры
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .paint(
//                painterResource(id = R.drawable.background),
//                contentScale = ContentScale.Crop
//            ), // Добавление фона как на картинке
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Верхняя панель с уровнем, временем и настройками
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.back),
//                contentDescription = "Menu",
//                modifier = Modifier.size(40.dp)
//            )
//
//            Text(
//                text = "LEVEL 1",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "TIME",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color.White
//                )
//                Text(
//                    text = "00:30",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//            }
//
//            Image(
//                painter = painterResource(id = R.drawable.settings_icon),
//                contentDescription = "Settings",
//                modifier = Modifier.size(40.dp)
//            )
//        }
//
//        // Сундуки и ключи
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Разделение сундуков на два ряда
//            for (i in 0..1) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//                    chestKeys.drop(i * 3).take(3).forEach { (chest, key) ->
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            // Сундук
//                            ChestView(
//                                chestImage = chest,
//                                isMatched = matchedKeys.any { it.first == chest },
//                                chestKey = chest,
//                                chestPositions = chestPositions
//                            )
//                            Spacer(modifier = Modifier.height(8.dp)) // Пробел между сундуком и полем для ключа
//                            // Поле для ключа
//                            val isMatched = matchedKeys.any { it.second == key }
//                            Box(
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .border(2.dp, if (isMatched) Color.Green else Color.Gray)
//                                    .background(if (isMatched) Color.Green else Color.LightGray),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                if (isMatched) {
//                                    KeyView(
//                                        keyImage = key,
//                                        isSelected = false
//                                    ) // Если ключ подошел, размещаем его в квадратике
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        // Секция с ключами (в случайном порядке) и перетаскиванием
//        Box(modifier = Modifier.fillMaxSize()) {
//            shuffledKeys.forEachIndexed { index, (_, key) ->
//                val isMatched = matchedKeys.any { it.second == key }
//
//                if (!keyInitialPositions.contains(key)) {
//                    // Генерируем начальную случайную позицию для ключа
//                    val randomXOffset = (50 * index).toFloat() // Пример случайного положения
//                    val initialPosition =
//                        Offset(randomXOffset, 600f) // Фиксированное положение по оси Y
//                    keyInitialPositions[key] = initialPosition
//                    keyCurrentPositions[key] =
//                        initialPosition // Начальная текущая позиция равна исходной
//                }
//
//                val currentPosition = keyCurrentPositions[key] ?: Offset(0f, 0f)
//                var offset by remember { mutableStateOf(currentPosition) }
//                var isDragging by remember { mutableStateOf(false) }
//
//                if (!isMatched) {  // Если ключ ещё не использован
//                    Box(
//                        modifier = Modifier
//                            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
//                            .pointerInput(Unit) {
//                                detectDragGestures(
//                                    onDragStart = {
//                                        isDragging = true
//                                    },
//                                    onDrag = { change, dragAmount ->
//                                        offset += dragAmount
//                                    },
//                                    onDragEnd = {
//                                        // Проверка на попадание в сундук
//                                        var droppedInChest = false
//                                        chestPositions.forEach { (chestId, chestCoordinates) ->
//                                            if (chestCoordinates != null && !matchedKeys.any { it.second == key }) {
//                                                val chestRect = chestCoordinates.boundsInParent()
//                                                val keyPosition = offset
//
//                                                if (keyPosition.x in chestRect.left..chestRect.right &&
//                                                    keyPosition.y in chestRect.top..chestRect.bottom
//                                                ) {
//                                                    matchedKeys.add(Pair(chestId, key))
//                                                    // Размещаем ключ в центр квадрата под сундуком
//                                                    val squarePosition =
//                                                        chestCoordinates
//                                                            .positionInParent()
//                                                            .copy(
//                                                                x = chestCoordinates.positionInParent().x + chestCoordinates.size.width / 2 - 30,
//                                                                y = chestCoordinates.positionInParent().y + chestCoordinates.size.height
//                                                            )
//                                                    offset = squarePosition
//                                                    droppedInChest = true
//                                                }
//                                            }
//                                        }
//                                        if (!droppedInChest) {
//                                            // Если не попали в сундук, возвращаем на начальную позицию
//                                            offset = keyInitialPositions[key] ?: Offset(0f, 0f)
//                                        }
//                                        keyCurrentPositions[key] =
//                                            offset // Обновляем текущую позицию
//                                        isDragging = false
//                                    }
//                                )
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        KeyView(
//                            keyImage = key,
//                            isSelected = isDragging
//                        )
//                    }
//                }
//            }
//        }
//
//        // Нижняя панель с прогрессом и ресурсами
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceAround,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "1/5",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//                Text(text = "CHRONO", fontSize = 14.sp, color = Color.White)
//            }
//            Text(text = "CHESTS OPENED 0/6", fontSize = 16.sp, color = Color.White)
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "1/5",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//                Text(text = "ENERGY", fontSize = 14.sp, color = Color.White)
//            }
//        }
//    }
//}
//
//// Компонент для сундука с записью позиции сундуков
//@Composable
//fun ChestView(
//    chestImage: Int,
//    isMatched: Boolean,
//    chestKey: Int,
//    chestPositions: MutableMap<Int, LayoutCoordinates>
//) {
//    Box(
//        modifier = Modifier
//            .size(80.dp)
//            .border(2.dp, if (isMatched) Color.Green else Color.Gray)
//            .background(Color(0xFFD9A88E))
//            .onGloballyPositioned { layoutCoordinates ->
//                chestPositions[chestKey] = layoutCoordinates
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = chestImage),
//            contentDescription = "Chest",
//            modifier = Modifier.size(60.dp)
//        )
//    }
//}
//
//// Компонент для ключа
//@Composable
//fun KeyView(
//    keyImage: Int,
//    isSelected: Boolean
//) {
//    Box(
//        modifier = Modifier
//            .size(60.dp)
//            .border(2.dp, if (isSelected) Color.Blue else Color.Gray),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = keyImage),
//            contentDescription = "Key",
//            modifier = Modifier.size(40.dp)
//        )
//    }
//}
//
//// Генерация сундуков и ключей (каждый сундук соответствует своему ключу)
//fun generateChestsAndKeys(): List<Pair<Int, Int>> {
//    return listOf(
//        Pair(R.drawable.chest_1, R.drawable.key_1),
//        Pair(R.drawable.chest_2, R.drawable.key_2),
//        Pair(R.drawable.chest_3, R.drawable.key_3),
//        Pair(R.drawable.chest_4, R.drawable.key_4),
//        Pair(R.drawable.chest_5, R.drawable.key_5),
//        Pair(R.drawable.chest_6, R.drawable.key_6)
//    )
//}



