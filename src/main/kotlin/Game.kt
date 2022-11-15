package com.mojang.escape

import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.LadderBlock
import com.mojang.escape.menu.*
import java.awt.event.KeyEvent
import kotlin.math.cos
import kotlin.math.sin

class Game {
    var time = 0
    var level: Level? = null
    var player: Player? = null
    var pauseTime = 0
    var menu: Menu? = TitleMenu()

    fun newGame() {
        Level.clear()

        val localLevel = Level.loadLevel(this, "start")
        val localPlayer = Player()

        localPlayer.level = localLevel
        localLevel.player = localPlayer
        localPlayer.x = localLevel.xSpawn.toDouble()
        localPlayer.z = localLevel.ySpawn.toDouble()
        localLevel.addEntity(localPlayer)
        localPlayer.rot = Math.PI + 0.4

        level = localLevel
        player = localPlayer
    }

    fun switchLevel(name: String, id: Int) {
        pauseTime = 30
        level!!.removeEntityImmediately(player!!)
        val localLevel = Level.loadLevel(this, name)
        val localPlayer = player!!

        localLevel.player = localPlayer
        localPlayer.level = localLevel
        localLevel.findSpawn(id)
        localPlayer.x = localLevel.xSpawn.toDouble()
        localPlayer.z = localLevel.ySpawn.toDouble()
        (localLevel.getBlock(localLevel.xSpawn, localLevel.ySpawn) as LadderBlock).wait = true
        localPlayer.x += sin(localPlayer.rot) * 0.2
        localPlayer.z += cos(localPlayer.rot) * 0.2
        localLevel.addEntity(localPlayer)

        level = localLevel
        player = localPlayer
    }

    fun tick(keys: BooleanArray) {
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

        if (menu != null) {
            menu?.tick(this, keys, keys[Keys.KEY_UP.ordinal], keys[Keys.KEY_DOWN.ordinal], keys[Keys.KEY_LEFT.ordinal], keys[Keys.KEY_RIGHT.ordinal], keys[Keys.KEY_ENTER.ordinal] || keys[Keys.KEY_SPACE.ordinal])

            keys[Keys.KEY_UP.ordinal] = false
            keys[Keys.KEY_DOWN.ordinal] = false
            keys[Keys.KEY_LEFT.ordinal] = false
            keys[Keys.KEY_RIGHT.ordinal] = false
            keys[Keys.KEY_ENTER.ordinal] = false
            keys[Keys.KEY_SPACE.ordinal] = false
        } else {
            val strafe = keys[Keys.KEY_CONTROL.ordinal] || keys[Keys.KEY_ALT.ordinal] || keys[Keys.KEY_ALT_GRAPH.ordinal] || keys[Keys.KEY_SHIFT.ordinal]

            val lk = keys[Keys.KEY_LEFT.ordinal] || keys[Keys.KEY_NUMPAD4.ordinal]
            val rk = keys[Keys.KEY_RIGHT.ordinal] || keys[Keys.KEY_NUMPAD6.ordinal]

            val up = keys[SettingsMenu.keyForward.ordinal] || keys[Keys.KEY_UP.ordinal] || keys[Keys.KEY_NUMPAD8.ordinal]
            val down = keys[SettingsMenu.keyBackward.ordinal] || keys[Keys.KEY_DOWN.ordinal] || keys[Keys.KEY_NUMPAD2.ordinal]
            val left = keys[SettingsMenu.keyStrafeLeft.ordinal] || (strafe && lk)
            val right = keys[SettingsMenu.keyStrafeRight.ordinal] || (strafe && rk)

            val turnLeft = keys[SettingsMenu.keyTurnLeft.ordinal] || (!strafe && lk)
            val turnRight = keys[SettingsMenu.keyTurnRight.ordinal] || (!strafe && rk)

            val use = keys[SettingsMenu.keyInteract.ordinal]

            if (use) {
                keys[SettingsMenu.keyInteract.ordinal] = false
            }

            for (i in 0 until 8) {
                if (keys[SettingsMenu.keySlots[i].ordinal]) {
                    keys[SettingsMenu.keySlots[i].ordinal] = false
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