package com.mojang.escape.level.provider

import com.mojang.escape.entities.Entity
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block

interface ILevelProvider {
    fun getName(): StringUnit
    fun getWallCol(): Int
    fun getWallTex(): Int
    fun getFloorCol(): Int
    fun getFloorTex(): Int
    fun getCeilCol(): Int
    fun getCeilTex(): Int
    fun getWidth(): Int
    fun getHeight(): Int
    fun getBlocks(level: Level): Array<Block>
    fun getEntities(level: Level): MutableList<Entity>
    fun getSpawn(): Pair<Int, Int>?
}