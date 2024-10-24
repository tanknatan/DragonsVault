package com.dragons.vault.keys.chests.data


import android.content.Context
import android.media.MediaPlayer


import com.dragons.vault.keys.chests.R


object SoundManager {
    private lateinit var musicPlayer: MediaPlayer
    private lateinit var soundPlayer: MediaPlayer
    private lateinit var gameMusicPlayer: MediaPlayer

    fun init(context: Context) = runCatching {
        musicPlayer = MediaPlayer.create(context, R.raw.music)
        soundPlayer = MediaPlayer.create(context, R.raw.sound)
        gameMusicPlayer = MediaPlayer.create(context, R.raw.game_music)
    }

    fun playMusic() = runCatching {
        musicPlayer.setVolume(Prefs.music, Prefs.music)
        musicPlayer.start()
        musicPlayer.isLooping = true
    }

    fun playSound() = runCatching {
        soundPlayer.setVolume(Prefs.sound, Prefs.sound)
        soundPlayer.start()
        soundPlayer.isLooping = false
    }

    fun playGameMusic() = runCatching {
        gameMusicPlayer.setVolume(Prefs.music, Prefs.music)
        gameMusicPlayer.start()
        gameMusicPlayer.isLooping = true
    }

    fun pauseMusic() = runCatching {
        musicPlayer.pause()

    }

    fun pauseSound() = runCatching {

        soundPlayer.pause()
    }

    fun pauseGameMusic() = runCatching {
        gameMusicPlayer.pause()
    }

    fun resumeMusic() = runCatching {
        if (!musicPlayer.isPlaying) {
            musicPlayer.start()
        }
    }

    fun resumeSound() = runCatching {
        if (!soundPlayer.isPlaying) {
            soundPlayer.start()
        }
    }

    fun resumeGameMusic() = runCatching {
        if (!gameMusicPlayer.isPlaying) {
            gameMusicPlayer.start()
        }
    }


    fun onDestroy() = runCatching {
        musicPlayer.release()
        soundPlayer.release()
        gameMusicPlayer.release()
    }

    fun setVolumeMusic() = runCatching {
        musicPlayer.setVolume(Prefs.music, Prefs.music)
    }

    fun setVolumeSound() = runCatching {
        soundPlayer.setVolume(Prefs.sound, Prefs.sound)
    }

    fun setVolumeGameMusic() = runCatching {
        gameMusicPlayer.setVolume(Prefs.music, Prefs.music)
    }


}