package com.mojang.escape

import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.input.InputHandler
import com.mojang.escape.lang.Language
import com.mojang.escape.level.Level
import com.mojang.escape.menu.*
import com.mojang.escape.menu.settings.GameSettings
import com.mojang.escape.mods.IMod

class Game(private val window: Long) {
    companion object {
        val symbols = Language("symbols")
    }
    
    var session: GameSession? = null

    var pauseTime = 0
    var menu: Menu? = TitleMenu()
    var scrollOffset: Double = 0.0
    private val inputHandler = InputHandler()

    fun newGame() {
        menu = GameTypePickerMenu(menu)
    }

    fun tick() {
        val menu = menu
        val session = session

        inputHandler.updateInputStates(this, window)
        inputHandler.handleInput(this, window)
        
        if (menu != null) {
            menu.onTick(this)
        }

        if (menu == null && session != null) {
            session.tick()
        }
    }
}