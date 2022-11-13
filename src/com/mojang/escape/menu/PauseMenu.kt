package com.mojang.escape.menu

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.Sound
import com.mojang.escape.gui.Bitmap

class PauseMenu: Menu() {
    private val options = arrayOf("Abort game", "Continue")
    private var selected = 1

    override fun render(target: Bitmap) {
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
    }

    override fun tick(game: Game, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
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
            game.menu = when (selected) {
                0 -> TitleMenu()
                else -> null
            }
        }
    }
}