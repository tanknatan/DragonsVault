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

    fun unlockNextLevel(levelCompleted: Int) {
        val currentUnlockedLevels = getUnlockedLevels()
        if (levelCompleted >= currentUnlockedLevels) {
            sharedPreferences.edit().putInt(UNLOCKED_LEVELS_KEY, levelCompleted + 1).apply()
        }
    }

    fun isLevelUnlocked(level: Int): Boolean {
        return level <= getUnlockedLevels()
    }

    fun getStarsForLevel(level: Int): Int {
        return sharedPreferences.getInt("$STARS_KEY$level", 0)
    }

    fun getCompletionDate(level: Int): String? {
        return sharedPreferences.getString("$DATE_KEY$level", null)
    }

    fun getCompletionTime(level: Int): String? {
        return sharedPreferences.getString("$TIME_KEY$level", null)
    }

    fun getCompletedLevels(): List<Int> {
        val completedLevels = mutableListOf<Int>()
        for (level in 1..getUnlockedLevels()) {
            if (getCompletionDate(level) != null) {
                completedLevels.add(level)
            }
        }
        return completedLevels
    }
}


