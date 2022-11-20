package com.mojang.escape.mods

import com.mojang.escape.Game
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level

interface IMod {
    val name: StringUnit
    
    fun getFirstLevel(game: Game): Level
    
    fun onNewGame(game: Game)
}