package com.mojang.escape

import com.mojang.escape.entities.Player
import com.mojang.escape.level.Level
import com.mojang.escape.mods.IMod

class GameSession(private val game: Game, val mod: IMod) {
    private val loadedLevels = mutableMapOf<String, Level>()
    
    var currentLevel: Level
    val player: Player
    
    var pauseTime = 0
    
    init {
        
    }
}