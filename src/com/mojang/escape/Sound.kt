package com.mojang.escape

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import kotlin.concurrent.thread

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
                thread {
                    synchronized(clip) {
                        clip.stop()
                        clip.framePosition = 0
                        clip.start()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}