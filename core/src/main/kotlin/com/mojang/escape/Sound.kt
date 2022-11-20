package com.mojang.escape

import com.mojang.escape.menu.settings.GameSettings
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import kotlin.math.log10

class Sound(private val clip: Clip?) {
    companion object {
        val altar = loadSound("/snd/altar.wav")
        val click1 = loadSound("/snd/click.wav")
        val click2 = loadSound("/snd/click2.wav")
        val hurt = loadSound("/snd/hurt.wav")
        val hurt2 = loadSound("/snd/hurt2.wav")
        val kill = loadSound("/snd/kill.wav")
        val pickup = loadSound("/snd/pickup.wav")
        val shoot = loadSound("/snd/shoot.wav")
        val potion = loadSound("/snd/potion.wav")
        val death = loadSound("/snd/death.wav")
        
        fun loadSound(fileName: String, clazz: Class<*>? = null): Sound {
            return try {
                val ais = AudioSystem.getAudioInputStream(if (clazz != null) {
                    clazz.getResource(fileName)
                } else {
                    Sound::class.java.getResource(fileName)
                })
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
                        val volPercentage = GameSettings.volume.value / 4.0f
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