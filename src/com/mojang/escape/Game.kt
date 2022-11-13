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
    lateinit var level: Level
    lateinit var player: Player
    var pauseTime = 0
    var menu: Menu? = TitleMenu()

    fun newGame() {
        Level.clear()
        level = Level.loadLevel(this, "start")

        player = Player()
        player.level = level
        level.player = player
        player.x = level.xSpawn.toDouble()
        player.z = level.ySpawn.toDouble()
        level.addEntity(player)
        player.rot = Math.PI + 0.4
    }

    fun switchLevel(name: String, id: Int) {
        pauseTime = 30
        level.removeEntityImmediately(player)
        level = Level.loadLevel(this, name)
        level.findSpawn(id)
        player.x = level.xSpawn.toDouble()
        player.z = level.ySpawn.toDouble()
        (level.getBlock(level.xSpawn, level.ySpawn) as LadderBlock).wait = true
        player.x += sin(player.rot) * 0.2
        player.z += cos(player.rot) * 0.2
        level.addEntity(player)
    }

    fun tick(keys: BooleanArray) {
        if (pauseTime > 0) {
            pauseTime--
            return
        }

        time++

        val strafe = keys[KeyEvent.VK_CONTROL] || keys[KeyEvent.VK_ALT] || keys[KeyEvent.VK_ALT_GRAPH] || keys[KeyEvent.VK_SHIFT]

        val lk = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_NUMPAD4]
        val rk = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_NUMPAD6]

        val up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_NUMPAD8]
        val down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_NUMPAD2]
        val left = keys[KeyEvent.VK_A] || (strafe && lk)
        val right = keys[KeyEvent.VK_D] || (strafe && rk)

        val turnLeft = keys[KeyEvent.VK_Q] || (!strafe && lk)
        val turnRight = keys[KeyEvent.VK_E] || (!strafe && rk)

        val use = keys[KeyEvent.VK_SPACE]

        for (i in 0 until 8) {
            if (keys[KeyEvent.VK_1 + i]) {
                keys[KeyEvent.VK_1 + i] = false
                player.selectedSlot = i
                player.itemUseTime = 0
            }
        }

        if (keys[KeyEvent.VK_ESCAPE]) {
            keys[KeyEvent.VK_ESCAPE] = false
            if (menu == null) {
                menu = PauseMenu()
            }
        }

        if (use) {
            keys[KeyEvent.VK_SPACE] = false
        }

        if (menu != null) {
            keys[KeyEvent.VK_W] = false
            keys[KeyEvent.VK_UP] = false
            keys[KeyEvent.VK_NUMPAD8] = false
            keys[KeyEvent.VK_S] = false
            keys[KeyEvent.VK_DOWN] = false
            keys[KeyEvent.VK_NUMPAD2] = false
            keys[KeyEvent.VK_A] = false
            keys[KeyEvent.VK_D] = false

            menu?.tick(this, up, down, left, right, use)
        } else {
            player.tick(up, down, left, right, turnLeft, turnRight)
            if (use) {
                player.activate()
            }

            level.tick()
        }
    }

    fun getLoot(item: Item) {
        player.addLoot(item)
    }

    fun win(player: Player) {
        menu = WinMenu(player)
    }

    fun lose(player: Player) {
        menu = LoseMenu(player)
    }
}