package com.mojang.escape.menu

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.Sound
import com.mojang.escape.gui.Bitmap

class AboutMenu: Menu() {
    /**
     * The number of ticks remaining until the Continue button is displayed.
     */
    private var tickDelay = 30

    override fun render(target: Bitmap) {
        target.fill(0, 0, 160, 120, 0)

        target.draw("About", 60, 8, Art.getCol(0xFFFFFF))

        val lines = arrayOf(
            "Prelude of the Chambered",
            "by Markus Persson.",
            "Made Aug 2011 for the",
            "21'st Ludum Dare compo.",
            "",
            "This game is freeware,",
            "and was made from scratch",
            "in just 48 hours."
        )

        lines.forEachIndexed { index, s ->
            target.draw(s, 4, 28 + index * 8, Art.getCol(0xA0A0A0))
        }

        if (tickDelay == 0) {
            target.draw("-> Continue", 40, target.height - 16, Art.getCol(0xFFFF80))
        }
    }

    override fun tick(game: Game, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (tickDelay > 0) {
            tickDelay--
        } else if (use) {
            Sound.click1.play()
            game.menu = TitleMenu()
        }
    }


}