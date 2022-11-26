package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.input.MenuInput

class PauseMenu(lastMenu: Menu? = null) : Menu(lastMenu) {
    private val options = arrayOf(
        "gui.menu.pause.buttonAbortGame".toTranslatable(),
        "gui.menu.pause.buttonSettings".toTranslatable(),
        "gui.menu.pause.buttonContinue".toTranslatable()
    )
    private var selected = 2

    override fun doRender(target: Bitmap) {
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

    override fun handleInputs(game: Game, inputs: MutableMap<MenuInput, Boolean>) {
        if (inputs[MenuInput.UP] == true || inputs[MenuInput.DOWN] == true) {
            Sound.click2.play()
        }
        if (inputs[MenuInput.UP] == true) {
            selected--
        }
        if (inputs[MenuInput.DOWN] == true) {
            selected++
        }
        if (selected < 0) {
            selected = 0
        }
        if (selected >= options.size) {
            selected = options.size - 1
        }
        if (inputs[MenuInput.CONFIRM] == true) {
            Sound.click1.play()
            game.menu = when (selected) {
                0 -> {
                    game.session = null
                    TitleMenu()
                }
                1 -> SettingsMenu(this)
                else -> null
            }
        }
        if (inputs[MenuInput.BACK] == true) {
            game.menu = null
        }
    }

    override fun onTick(game: Game) {
        // Do nothing
    }
}