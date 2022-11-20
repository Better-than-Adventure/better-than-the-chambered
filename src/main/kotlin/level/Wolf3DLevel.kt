package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.level.provider.Wolf3DLevelProvider
import com.mojang.escape.level.wolf3d.GameMaps
import com.mojang.escape.level.wolf3d.MapHead

class Wolf3DLevel(game: Game, private val levelProvider: Wolf3DLevelProvider) : Level(game, levelProvider) {
    companion object {
        val gameMaps: GameMaps by lazy { 
            Wolf3DLevel::class.java.getResourceAsStream("/wolf3d/MAPHEAD.WL6")!!.use { mapheadStream ->
                Wolf3DLevel::class.java.getResourceAsStream("/wolf3d/GAMEMAPS.WL6")!!.use { gamemapsStream ->
                    val mapheadBytes = mapheadStream.readBytes()
                    val gamemapsBytes = gamemapsStream.readBytes()
                    
                    val mapHead = MapHead(mapheadBytes)
                    GameMaps(gamemapsBytes, mapHead)
                }
            }
        }

        fun loadWolf3DLevel(game: Game, levelHeader: GameMaps.LevelHeader): Wolf3DLevel {
            val levelProvider = Wolf3DLevelProvider(levelHeader)

            val level = Wolf3DLevel(game, levelProvider)
            level.postInit()
            loaded[levelHeader.name] = level

            return level
        }
    }

    override fun switchLevel(id: Int) {
        val newGameMap = gameMaps.levelHeaders[gameMaps.levelHeaders.indexOf(levelProvider.levelHeader) + 1]
        game.switchLevel(loadWolf3DLevel(game, newGameMap))
    }
}