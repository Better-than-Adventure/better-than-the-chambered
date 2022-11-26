package com.mojang.escape.mods

import com.mojang.escape.Game
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.provider.ILevelProvider

interface IMod {
    val name: StringUnit
    
    val levelProvider: ILevelProvider
    
    fun onNewGame(game: Game)
}