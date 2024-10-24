package com.dragons.vault.keys.chests.data

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private lateinit var prefs: SharedPreferences

    private const val KEY_START_STEP_COMPLETED = "StartStepCompleted"

    fun init(context: Context) {
        prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }


    var music: Float
        get() = prefs.getFloat("music", 1f)
        set(value) {
            prefs.edit().putFloat("music", value).apply()
        }
    var sound: Float
        get() = prefs.getFloat("sound", 1f)
        set(value) {
            prefs.edit().putFloat("sound", value).apply()
        }
}