package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap

class PauseMenu(lastMenu: Menu? = null) : Menu(lastMenu) {
    private val options = arrayOf(
        "gui.menu.pause.buttonAbortGame".toTranslatable(),
        "gui.menu.pause.buttonSettings".toTranslatable(),
        "gui.menu.pause.buttonContinue".toTranslatable()
    )
    private var selected = 2

    override fun render(target: Bitmap) {
        target.draw(Art.logo, 0, 8, 0, 0, 160, 36, Art.getCol(0xFFFFFF))

        options.forEachIndexed { index, s ->
            var col = 0x909090
            if (selected == index) {
                val msg = "-> ".toLiteral() + s
                col = 0xFFFF80
                target.draw(msg, 40, 60 + index * 10, Art.getCol(col))
            } else {
                target.draw(s, 40, 60 + index * 10, Art.getCol(col))
            }
        }
    }

    override fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
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
                0 -> {
                    game.level = null
                    game.player = null
                    TitleMenu()
                }
                1 -> SettingsMenu(this)
                else -> null
            }
        }
    }
}