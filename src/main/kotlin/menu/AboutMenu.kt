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

        target.draw("gui.menu.about.title".translatable, 60, 8, Art.getCol(0xFFFFFF))

        val lines = arrayOf(
            "gui.menu.about.line0".translatable,
            "gui.menu.about.line1".translatable,
            "gui.menu.about.line2".translatable,
            "gui.menu.about.line3".translatable,
            "gui.menu.about.line4".translatable,
            "gui.menu.about.line5".translatable,
            "gui.menu.about.line6".translatable,
            "gui.menu.about.line7".translatable,
        )

        lines.forEachIndexed { index, s ->
            target.draw(s, 4, 28 + index * 8, Art.getCol(0xA0A0A0))
        }

        if (tickDelay == 0) {
            target.draw("-> " and "gui.menu.about.buttonContinue".translatable, 40, target.height - 16, Art.getCol(0xFFFF80))
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