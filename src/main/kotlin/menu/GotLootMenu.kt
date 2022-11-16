package com.mojang.escape.menu

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.and
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.translatable

class GotLootMenu(private val item: Item, lastMenu: Menu? = null): Menu(lastMenu) {
    /**
     * The number of ticks remaining until the Continue button is displayed.
     */
    private var tickDelay = 30

    override fun render(target: Bitmap) {
        val str = "gui.menu.gotLoot.text".translatable.format(item.itemName)
        target.scaleDraw(Art.items, 3, target.width / 2 - 8 * 3, 2, item.icon * 16, 0, 16, 16, Art.getCol(item.color))
        target.draw(str, (target.width - str.length * 6) / 2 + 2, 60 - 10, Art.getCol(0xFFFF80))

        target.draw(item.description, (target.width - item.description.length * 6) / 2 + 2, 60, Art.getCol(0xA0A0A0))

        if (tickDelay == 0) {
            target.draw("-> " and "gui.menu.gotLoot.buttonContinue".translatable, 40, target.height - 40, Art.getCol(0xFFFF80))
        }
    }

    override fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (tickDelay > 0) {
            tickDelay--
        } else if (use) {
            game.menu = null
        }
    }
}