package com.mojang.escape.menu

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap

abstract class Menu(val lastMenu: Menu?) {
    /**
     * Renders the menu to the [target] bitmap.
     */
    abstract fun render(target: Bitmap)

    /**
     * Updates the menu's content.
     *
     * [up], [down], [left], [right], and [use] are set to true when the corresponding key is pressed, i.e. UP ARROW, DOWN ARROW, LEFT ARROW, RIGHT ARROW and SPACEBAR respectively.
     */
    abstract fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean)
}