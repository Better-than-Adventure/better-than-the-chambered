package com.mojang.escape.mods.prelude

import com.mojang.escape.Sound

object ModSound {
    val bosskill = Sound.loadSound("/snd/bosskill.wav", this::class.java)
    val hit = Sound.loadSound("/snd/hit.wav", this::class.java)
    val splash = Sound.loadSound("/snd/splash.wav", this::class.java)
    val key = Sound.loadSound("/snd/key.wav", this::class.java)
    val roll = Sound.loadSound("/snd/roll.wav", this::class.java)
    val treasure = Sound.loadSound("/snd/treasure.wav", this::class.java)
    val crumble = Sound.loadSound("/snd/crumble.wav", this::class.java)
    val slide = Sound.loadSound("/snd/slide.wav", this::class.java)
    val cut = Sound.loadSound("/snd/cut.wav", this::class.java)
    val thud = Sound.loadSound("/snd/thud.wav", this::class.java)
    val ladder = Sound.loadSound("/snd/ladder.wav", this::class.java)
}