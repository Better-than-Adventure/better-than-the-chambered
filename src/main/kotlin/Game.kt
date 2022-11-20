package com.mojang.escape

import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.lang.Language
import com.mojang.escape.level.Level
import com.mojang.escape.level.Wolf3DLevel
import com.mojang.escape.level.block.LadderBlock
import com.mojang.escape.menu.*
import com.mojang.escape.menu.settings.GameSettings
import kotlin.math.cos
import kotlin.math.sin

class Game {
    companion object {
        val symbols = Language("symbols")
        var theGame: Game? = null
    }

    var time = 0
    var level: Level? = null
    var player: Player? = null
    var pauseTime = 0
    var menu: Menu? = TitleMenu()
    var gameType = GameType.WOLF3D

    fun newGame() {
        Level.clear()

        val localLevel = gameType.getFirstLevel(this)
        val localPlayer = Player()

        localPlayer.level = localLevel
        localLevel.player = localPlayer
        localPlayer.x = localLevel.xSpawn.toDouble()
        localPlayer.z = localLevel.ySpawn.toDouble()
        localLevel.addEntity(localPlayer)
        localPlayer.rot = Math.PI + 0.4

        level = localLevel
        player = localPlayer
        
        gameType.onNewGame(this)
    }

    fun switchLevel(level: Level) {
        pauseTime = 30
        this.level?.removeEntityImmediately(player!!)
        val localPlayer = player!!

        level.player = localPlayer
        localPlayer.level = level
        localPlayer.x = level.xSpawn.toDouble()
        localPlayer.z = level.ySpawn.toDouble()
        localPlayer.x += sin(localPlayer.rot) * 0.2
        localPlayer.z += cos(localPlayer.rot) * 0.2
        level.addEntity(localPlayer)

        this.level = level
        player = localPlayer
    }

    fun tick(keys: BooleanArray, mousePos: Pair<Double, Double>, mouseButtons: BooleanArray) {
        if (pauseTime > 0) {
            pauseTime--
            return
        }

        time++

        if (keys[Keys.KEY_ESCAPE.ordinal]) {
            keys[Keys.KEY_ESCAPE.ordinal] = false
            if (menu == null) {
                menu = PauseMenu()
            }
        }

        if (keys[Keys.KEY_G.ordinal]) {
            keys[Keys.KEY_G.ordinal] = false
            GameSettings.graphics.onActivated()
        }

        if (menu != null) {
            menu?.tick(this, keys, keys[Keys.KEY_UP.ordinal], keys[Keys.KEY_DOWN.ordinal], keys[Keys.KEY_LEFT.ordinal], keys[Keys.KEY_RIGHT.ordinal], keys[Keys.KEY_ENTER.ordinal] || keys[Keys.KEY_SPACE.ordinal])

            keys[Keys.KEY_UP.ordinal] = false
            keys[Keys.KEY_DOWN.ordinal] = false
            keys[Keys.KEY_LEFT.ordinal] = false
            keys[Keys.KEY_RIGHT.ordinal] = false
            keys[Keys.KEY_ENTER.ordinal] = false
            keys[Keys.KEY_SPACE.ordinal] = false
        } else {
            val strafe = keys[Keys.KEY_LEFT_CONTROL.ordinal] || keys[Keys.KEY_LEFT_ALT.ordinal] || keys[Keys.KEY_LEFT_SHIFT.ordinal]

            val lk = keys[Keys.KEY_LEFT.ordinal] || keys[Keys.KEY_KP_4.ordinal]
            val rk = keys[Keys.KEY_RIGHT.ordinal] || keys[Keys.KEY_KP_6.ordinal]

            val up = keys[GameSettings.keyForward.value.ordinal] || keys[Keys.KEY_UP.ordinal] || keys[Keys.KEY_KP_8.ordinal]
            val down = keys[GameSettings.keyBackward.value.ordinal] || keys[Keys.KEY_DOWN.ordinal] || keys[Keys.KEY_KP_2.ordinal]
            val left = keys[GameSettings.keyStrafeLeft.value.ordinal] || (strafe && lk)
            val right = keys[GameSettings.keyStrafeRight.value.ordinal] || (strafe && rk)

            var turnLeft = keys[GameSettings.keyTurnLeft.value.ordinal] || (!strafe && lk)
            var turnRight = keys[GameSettings.keyTurnRight.value.ordinal] || (!strafe && rk)

            val use = keys[GameSettings.keyInteract.value.ordinal] || mouseButtons[0]

            if (use) {
                keys[GameSettings.keyInteract.value.ordinal] = false
                mouseButtons[0] = false
            }

            if (GameSettings.mouseLook.value) {
                if (mousePos.first < 0) 
                    player?.rot = player?.rot?.minus(mousePos.first * 0.01)!!
                else if (mousePos.first > 0) 
                    player?.rot = player?.rot?.minus(mousePos.first * 0.01)!!
            }

            for (i in 0 until 8) {
                if (keys[GameSettings.keySlots[i].value.ordinal]) {
                    keys[GameSettings.keySlots[i].value.ordinal] = false
                    player?.selectedSlot = i
                    player?.itemUseTime = 0
                }
            }

            player?.tick(up, down, left, right, turnLeft, turnRight)
            if (use) {
                player?.activate()
            }

            level?.tick()
        }
    }

    fun getLoot(item: Item) {
        player?.addLoot(item)
    }

    fun win(player: Player) {
        menu = WinMenu(player)
    }

    fun lose(player: Player) {
        menu = LoseMenu(player)
    }
}