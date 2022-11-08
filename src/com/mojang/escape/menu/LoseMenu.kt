package com.mojang.escape.menu

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.Sound
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Bitmap

class LoseMenu(private val player: Player): Menu() {
    /**
     * The number of ticks remaining until the Continue button is displayed.
     */
    private var tickDelay = 30

    override fun render(target: Bitmap) {
        target.draw(Art.logo, 0, 10, 0, 39, 160, 23, Art.getCol(0xFFFFFF))

        val seconds = (player.time / 60) % 60
        val minutes = (player.time / 60) / 60

        target.draw("Trinkets: " + player.loot + "/12", 40, 45 + 10 * 0, Art.getCol(0x909090))
        target.draw("Time: ${"%d:%02d".format(minutes, seconds)}", 40, 45 + 10 * 1, Art.getCol(0x909090))

        if (tickDelay == 0) {
            target.draw("-> Continue", 40, target.height - 40, Art.getCol(0xFFFF80))
        }
    }

    override fun tick(game: Game, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (tickDelay > 0) {
            tickDelay--
        } else if (use) {
            Sound.click1.play()
            game.setMenu(TitleMenu())
        }
    }

}