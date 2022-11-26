package com.mojang.escape.menu

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.input.MenuInput

abstract class Menu(val lastMenu: Menu?) {
    /**
     * Renders the menu to the [target] bitmap.
     */
    abstract fun doRender(target: Bitmap)

    /**
     * Updates the menu's content.
     */
    abstract fun onTick(game: Game)
    
    abstract fun handleInputs(game: Game, inputs: MutableMap<MenuInput, Boolean>)
}