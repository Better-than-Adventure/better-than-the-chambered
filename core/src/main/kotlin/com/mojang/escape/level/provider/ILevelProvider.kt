package com.mojang.escape.level.provider

import com.mojang.escape.GameSession
import com.mojang.escape.entities.Entity
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block

interface ILevelProvider {
    fun getFirstLevel(session: GameSession): Level
    fun getLevelByName(session: GameSession, name: String): Level?
}