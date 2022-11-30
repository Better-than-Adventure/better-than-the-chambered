package com.mojang.escape

import com.mojang.escape.entities.Player
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.ILevelChangeBlock
import com.mojang.escape.level.physics.Point3
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.menu.Menu
import com.mojang.escape.mods.IMod
import kotlin.math.cos
import kotlin.math.sin

class GameSession(private val game: Game, val mod: IMod) {
    var currentLevel: Level
    val player: Player = Player(this, Point3(0.0, 0.0, 0.0), 0.0, Point3D(0.0, 0.0, 0.0), 0.0)
    
    init {
        val level = mod.levelProvider.getFirstLevel(this)

        level.entities.add(player)
        player.pos = Point3D(level.spawn.x.toDouble(), 0.0, level.spawn.z.toDouble())
        player.pos = player.pos.copy(
            x = player.pos.x + sin(player.rot) * 0.2,
            z = player.pos.z + cos(player.rot) * 0.2
        )
        currentLevel = level
    }
    
    fun tick() {
        currentLevel.onTick()
    }
    
    fun switchLevel(name: String, levelChangeId: Int) {
        val newLevel = mod.levelProvider.getLevelByName(this, name)
        if (newLevel != null) {
            switchLevel(newLevel, levelChangeId)
        } else {
            println("Could not switch to level $name!")
        }
    }
    
    private fun switchLevel(level: Level, levelChangeId: Int) {
        currentLevel.entities.remove(player)
        
        level.entities.add(player)
        var newPos = Point3D(level.spawn.x.toDouble(), 0.0, level.spawn.z.toDouble())
        for (block in level.blocks) {
            if (block is ILevelChangeBlock) {
                if (block.levelChangeIdIn == levelChangeId) {
                    newPos = Point3D(block.pos.x.toDouble(), 0.0, block.pos.z.toDouble())
                    break
                }
            }
        }
        player.pos = newPos
        currentLevel = level
    }
    
    fun showMenu(menu: Menu) {
        game.menu = menu
    }
}