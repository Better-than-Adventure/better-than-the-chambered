package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.input.MenuInput

class TitleMenu(lastMenu: Menu? = null) : Menu(lastMenu) {
    private val options = arrayOf(
        "gui.menu.title.buttonNewGame".toTranslatable(),
        "gui.menu.title.buttonSettings".toTranslatable(),
        "gui.menu.title.buttonAbout".toTranslatable()
    )
    private var selected = 0
    private var firstTick = true

    override fun doRender(target: Bitmap) {
        target.fill(0, 0, 160, 120, 0)
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
        target.draw("gui.menu.title.copyright".toTranslatable(), 1 + 4, 120 - 9, Art.getCol(0x303030))
    }

    override fun onTick(game: Game) {
        if (firstTick) {
            Sound.altar.play()
            firstTick = false
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
            when (selected) {
                0 -> {
                    game.menu = null
                    game.newGame()
                }
                1 -> {
                    game.menu = SettingsMenu(this)
                }
                2 -> {
                    game.menu = AboutMenu()
                }
            }
        }

    }
 }