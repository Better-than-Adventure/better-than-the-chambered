package com.mojang.escape

import com.mojang.escape.menu.SettingsMenu
import com.mojang.escape.menu.settings.GameSettings
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import kotlin.math.log10
import kotlin.math.pow

class Sound(private val clip: Clip?) {
    companion object {
        val altar = loadSound("/snd/altar.wav")
        val bosskill = loadSound("/snd/bosskill.wav")
        val click1 = loadSound("/snd/click.wav")
        val click2 = loadSound("/snd/click2.wav")
        val hit = loadSound("/snd/hit.wav")
        val hurt = loadSound("/snd/hurt.wav")
        val hurt2 = loadSound("/snd/hurt2.wav")
        val kill = loadSound("/snd/kill.wav")
        val death = loadSound("/snd/death.wav")
        val splash = loadSound("/snd/splash.wav")
        val key = loadSound("/snd/key.wav")
        val pickup = loadSound("/snd/pickup.wav")
        val roll = loadSound("/snd/roll.wav")
        val shoot = loadSound("/snd/shoot.wav")
        val treasure = loadSound("/snd/treasure.wav")
        val crumble = loadSound("/snd/crumble.wav")
        val slide = loadSound("/snd/slide.wav")
        val cut = loadSound("/snd/cut.wav")
        val thud = loadSound("/snd/thud.wav")
        val ladder = loadSound("/snd/ladder.wav")
        val potion = loadSound("/snd/potion.wav")

        private fun loadSound(fileName: String): Sound {
            return try {
                val ais = AudioSystem.getAudioInputStream(Sound::class.java.getResource(fileName))
                val clip = AudioSystem.getClip()
                clip.open(ais)
                Sound(clip)
            } catch (e: Exception) {
                e.printStackTrace()
                Sound(null)
            }
        }
    }

    fun play() {
        if (clip != null) {
            try {
                Thread {
                    synchronized(clip) {
                        val fc = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
                        val volPercentage = GameSettings.volume / 4.0f
                        var dbs = 10 * log10(503570175.0 * volPercentage * volPercentage * volPercentage * volPercentage * volPercentage) + fc.minimum - 1
                        clip.stop()
                        clip.framePosition = 0
                        fc.value = dbs.toFloat()
                        clip.start()
                    }
                }.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}