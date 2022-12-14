package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap

class AboutMenu(lastMenu: Menu? = null) : Menu(lastMenu) {
    /**
     * The number of ticks remaining until the Continue button is displayed.
     */
    private var tickDelay = 30

    override fun render(target: Bitmap) {
        target.fill(0, 0, 160, 120, 0)

        target.draw("gui.menu.about.title".toTranslatable(), 60, 8, Art.getCol(0xFFFFFF))

        val lines = arrayOf(
            "gui.menu.about.line0".toTranslatable(),
            "gui.menu.about.line1".toTranslatable(),
            "gui.menu.about.line2".toTranslatable(),
            "gui.menu.about.line3".toTranslatable(),
            "gui.menu.about.line4".toTranslatable(),
            "gui.menu.about.line5".toTranslatable(),
            "gui.menu.about.line6".toTranslatable(),
            "gui.menu.about.line7".toTranslatable(),
        )

        lines.forEachIndexed { index, s ->
            target.draw(s, 4, 28 + index * 8, Art.getCol(0xA0A0A0))
        }

        if (tickDelay == 0) {
            target.draw("-> ".toLiteral() + "gui.menu.about.buttonContinue".toTranslatable(), 40, target.height - 16, Art.getCol(0xFFFF80))
        }
    }

    override fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (tickDelay > 0) {
            tickDelay--
        } else if (use) {
            Sound.click1.play()
            game.menu = TitleMenu()
        }
    }


}