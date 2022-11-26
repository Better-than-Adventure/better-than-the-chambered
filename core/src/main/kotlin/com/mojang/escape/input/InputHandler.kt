package com.mojang.escape.input

import com.mojang.escape.Game
import com.mojang.escape.GameSession
import com.mojang.escape.menu.PauseMenu
import com.mojang.escape.menu.settings.GameSettings
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack

class InputHandler {
    private var lastMousePos = Pair(0.0, 0.0)
    private var mouseDelta = Pair(0.0, 0.0)

    private var menuInputs = mutableMapOf<MenuInput, Boolean>()
    private var gameInputs = mutableMapOf<GameInput, Boolean>()
    private var lastMenuInputs = mutableMapOf<MenuInput, Boolean>()
    private var lastGameInputs = mutableMapOf<GameInput, Boolean>()
    
    private var lastScrollOffset = 0.0
    
    fun handleInput(game: Game, window: Long) {
        val menu = game.menu
        val session = game.session
        
        // Game is in menu
        if (menu != null) {
            menu.handleInputs(game, menuInputs)
        }
        
        // Game is ingame and unpaused
        if (menu == null && session != null) {
            if (gameInputs[GameInput.PAUSE] == true) {
                game.menu = PauseMenu()
            }
            
            session.player.xm = 0
            session.player.zm = 0
            
            // Forward
            if (gameInputs[GameInput.FORWARD] == true) {
                session.player.zm--
            }
            // Backward
            if (gameInputs[GameInput.BACKWARD] == true) {
                session.player.zm++
            }
            // Strafe Left
            if (gameInputs[GameInput.STRAFE_LEFT] == true || 
                (gameInputs[GameInput.TURN_LEFT] == true &&
                        gameInputs[GameInput.STRAFE_MOD] == true)
            ) {
                session.player.xm--
            }
            // Strafe Right
            if (gameInputs[GameInput.STRAFE_RIGHT] == true ||
                (gameInputs[GameInput.TURN_RIGHT] == true &&
                        gameInputs[GameInput.STRAFE_MOD] == true)) {
                session.player.xm++
            }
            // Turn Left
            if (gameInputs[GameInput.TURN_LEFT] == true &&
                gameInputs[GameInput.STRAFE_MOD] == false
            ) {
                session.player.rotVel += 0.05
            }
            // Turn Right
            if (gameInputs[GameInput.STRAFE_LEFT] == true &&
                gameInputs[GameInput.STRAFE_MOD] == true) {
                session.player.rotVel -= 0.05
            }
            // Use
            if (gameInputs[GameInput.USE] == true) {
                session.player.activate(session.currentLevel)
            }
            // Slots
            for (i in 0 until 8) {
                if (gameInputs[GameInput.values()[GameInput.SLOT_1.ordinal + i]] == true) {
                    session.player.selectedSlot = i
                    session.player.itemUseTime = 0
                }
            }
            if (gameInputs[GameInput.SLOT_LAST] == true) {
                session.player.selectedSlot = session.player.selectedSlot - 1
                session.player.itemUseTime = 0
                if (session.player.selectedSlot < 0) session.player.selectedSlot += 8
            }
            if (gameInputs[GameInput.SLOT_NEXT] == true) {
                session.player.selectedSlot = session.player.selectedSlot + 1
                session.player.itemUseTime = 0
                if (session.player.selectedSlot >= 8) session.player.selectedSlot -= 8
            }
            
            if (GameSettings.mouseLook.value) {
                session.player.rot -= mouseDelta.first * 0.01
            }
        }
    }
    
    fun updateInputStates(game: Game, window: Long) {
        menuInputs = mutableMapOf()
        gameInputs = mutableMapOf()
        var mouseDelta = Pair(0.0, 0.0)
        
        // Game is in menu
        if (game.menu != null) {
            MenuInput.values().forEach {  highLevelInput ->
                val anyKeyPressed = highLevelInput.inputs.any {  lowLevelInput ->
                    lowLevelInput.isPressed(window)
                }
                menuInputs[highLevelInput] = anyKeyPressed && !lastMenuInputs.getOrDefault(highLevelInput, false)
                lastMenuInputs[highLevelInput] = anyKeyPressed
            }
        }
        
        // Game is ingame and unpaused
        if (game.menu == null && game.session != null) {
            GameInput.values().forEach {  highLevelInput ->
                val anyKeyPressed = highLevelInput.inputs.any {  lowLevelInput ->
                    when (lowLevelInput) {
                        Input.Mouse.MWHEEL_UP -> game.scrollOffset > lastScrollOffset
                        Input.Mouse.MWHEEL_DOWN -> game.scrollOffset < lastScrollOffset
                        else -> lowLevelInput.isPressed(window)
                    }
                } 
                gameInputs[highLevelInput] = anyKeyPressed && if (highLevelInput.continuous) {
                    !lastGameInputs.getOrDefault(highLevelInput, false)
                } else {
                    true
                }
                lastGameInputs[highLevelInput] = anyKeyPressed
            }
            
            // Get mouse pos
            MemoryStack.stackPush().use { stack ->
                val x = stack.mallocDouble(1)
                val y = stack.mallocDouble(1)

                glfwGetCursorPos(window, x, y)

                mouseDelta = Pair(x[0] - lastMousePos.first, y[0] - lastMousePos.second)
                lastMousePos = Pair(x[0], y[0])
            }
        }
        this.lastGameInputs = gameInputs
        this.mouseDelta = mouseDelta
        this.lastScrollOffset = game.scrollOffset
    }
}