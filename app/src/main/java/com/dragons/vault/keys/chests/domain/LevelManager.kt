package com.dragons.vault.keys.chests.domain

import android.content.SharedPreferences


class LevelManager(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val UNLOCKED_LEVELS_KEY = "unlockedLevels"
        private const val STARS_KEY = "starsLevel"
        private const val DATE_KEY = "completionDate"
        private const val TIME_KEY = "completionTime"
    }

    fun getUnlockedLevels(): Int {
        return sharedPreferences.getInt(UNLOCKED_LEVELS_KEY, 1)
    }

    fun isLevelUnlocked(level: Int): Boolean {
        return sharedPreferences.getBoolean(
            "level_${level}_unlocked",
            level == 1
        ) // Первый уровень всегда открыт
    }

    fun getStarsForLevel(level: Int): Int {
        return sharedPreferences.getInt("level_${level}_stars", 0) // По умолчанию 0 звезд
    }

    fun unlockNextLevel(levelCompleted: Int) {
        val currentUnlockedLevels = getUnlockedLevels()
        if (levelCompleted >= currentUnlockedLevels) {
            sharedPreferences.edit().putInt(UNLOCKED_LEVELS_KEY, levelCompleted + 1).apply()
        }
    }
    fun getCompletedLevels(): List<Int> {
        val completedLevels = mutableListOf<Int>()
        for (level in 1..9) { // Предположим, что у нас всего 9 уровней
            if (sharedPreferences.getBoolean("level_${level}_completed", false)) {
                completedLevels.add(level)
            }
        }
        return completedLevels
    }

    fun getCompletionDate(level: Int): String? {
        return sharedPreferences.getString("level_${level}_completion_date", null)
    }

    fun getCompletionTime(level: Int): String? {
        val time = sharedPreferences.getInt("level_${level}_completion_time", -1)
        return if (time != -1) "$time seconds" else null
    }
}


