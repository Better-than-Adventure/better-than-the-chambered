package com.mojang.escape.menu

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.Sound
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.json.JsonHandler
import com.mojang.escape.json.JsonHandler.Companion.fromJson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.InputStream
import java.nio.charset.Charset

@Serializable
data class Item(
    @SerialName("test") val test: String,
)

class TitleMenu: Menu() {
    private val options = arrayOf("New game", "Settings", "About")
    private var selected = 0
    private var firstTick = true


    override fun render(target: Bitmap) {
        target.fill(0, 0, 160, 120, 0)
        target.draw(Art.logo, 0, 8, 0, 0, 160, 36, Art.getCol(0xFFFFFF))

        options.forEachIndexed { index, s ->
            var msg = s
            var col = 0x909090
            if (selected == index) {
                msg = "-> $msg"
                col = 0xFFFF80
            }
            target.draw(msg, 40, 60 + index * 10, Art.getCol(col))
        }

        val inputStream = JsonHandler.readFile("/json/hello_world.json")?.fromJson<Item>();
        if (inputStream != null) {
            target.draw(inputStream.test, 1 + 4, 120 - 9, Art.getCol(0x303030))
        }
        //target.draw("Copyright (C) 2011 Mojang", 1 + 4, 120 - 9, Art.getCol(0x303030))
    }

    override fun tick(game: Game, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (firstTick) {
            firstTick = false
            Sound.altar.play()
        }
        if (up || down) {
            Sound.click2.play()
        }
        if (up) {
            selected--
        }
        if (down) {
            selected++
        }
        if (selected < 0) {
            selected = 0
        }
        if (selected >= options.size) {
            selected = options.size - 1
        }
        if (use) {
            Sound.click1.play()
            when (selected) {
                0 -> {
                    game.menu = null
                    game.newGame()
                }
                1 -> {
                    game.menu = SettingsMenu()
                }
                2 -> {
                    game.menu = AboutMenu()
                }
            }
        }
    }


}