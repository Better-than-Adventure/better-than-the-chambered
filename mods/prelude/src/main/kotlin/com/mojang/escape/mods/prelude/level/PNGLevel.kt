package com.mojang.escape.mods.prelude.level

import com.mojang.escape.Game
import com.mojang.escape.level.Level
import com.mojang.escape.mods.prelude.level.block.LadderBlock
import com.mojang.escape.mods.prelude.level.provider.PNGLevelProvider
import java.lang.Exception
import java.lang.RuntimeException

abstract class PNGLevel(game: Game, levelProvider: PNGLevelProvider) : Level(game, levelProvider) {
    companion object {
        fun loadPNGLevel(game: Game, name: String): PNGLevel {
            if (loaded.containsKey(name)) {
                return loaded.getValue(name) as PNGLevel
            }

            try {
                val levelProvider = PNGLevelProvider(
                    name,
                    wallCol = when (name) {
                        "crypt" -> 0x404040
                        "dungeon" -> 0xC64954
                        "ice" -> 0x6BE8FF
                        "overworld" -> 0xA0A0A0
                        "temple" -> 0xCFADD8
                        else -> 0xB3CEE2
                    },
                    floorCol = when (name) {
                        "crypt" -> 0x404040
                        "dungeon" -> 0x8E4A51
                        "ice" -> 0xB8DBE0
                        "overworld" -> 0x508253
                        "temple" -> 0x8A6496
                        else -> 0x9CA09B
                    },
                    ceilCol = when (name) {
                        "crypt" -> 0x404040
                        "dungeon" -> 0x8E4A51
                        "ice" -> 0xB8DBE0
                        "temple" -> 0x8A6496
                        else -> 0x9CA09B
                    },
                    wallTex = when (name) {
                        else -> 0
                    },
                    floorTex = when (name) {
                        "overworld" -> 8 + 3
                        else -> 0
                    },
                    ceilTex = when (name) {
                        "overworld" -> -1
                        else -> 0
                    }
                )

                val level = try {
                    val className = name.substring(0, 1).uppercase() + name.substring(1) + "Level"
                    Class.forName("com.mojang.escape.mods.prelude.level.$className").constructors[0].newInstance(game, levelProvider) as PNGLevel
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }

                level.postInit()
                loaded[name] = level
                
                return level
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
    
    private fun findSpawn(id: Int) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val b = blocks[x + y * width]
                if (b.id == id && b is LadderBlock) {
                    xSpawn = x
                    ySpawn = y
                }
            }
        }
    }
    
    protected fun switchLevel(name: String, id: Int) {
        val newLevel = loadPNGLevel(game, name)
        newLevel.findSpawn(id)
        (newLevel.getBlock(newLevel.xSpawn, newLevel.ySpawn) as LadderBlock).wait = true
        game.switchLevel(newLevel)
    }
}