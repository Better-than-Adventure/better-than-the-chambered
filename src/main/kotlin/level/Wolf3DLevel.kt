package com.mojang.escape.level

import com.mojang.escape.Art
import com.mojang.escape.EscapeComponent
import com.mojang.escape.Game
import com.mojang.escape.col
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.DoorBlock
import com.mojang.escape.level.block.SolidBlock
import com.mojang.escape.level.wolf3d.GameMaps

class Wolf3DLevel(private val levelHeader: GameMaps.LevelHeader): Level() {
    companion object {
        fun loadWolf3dLevel(game: Game, index: Int): Level {
            val wolfLevel = EscapeComponent.wolf3D.levelHeaders[index]
            
            if (loaded.containsKey(wolfLevel.name)) {
                return loaded.getValue(wolfLevel.name)
            }
            
            val level = Wolf3DLevel(wolfLevel)
            level.init(game, wolfLevel.name, 0, 0, IntArray(0))
            loaded[wolfLevel.name] = level
            
            return level
        }
    }
    override fun init(game: Game, name: String, w: Int, h: Int, pixels: IntArray) {
        this.game = game
        
        solidWall.col = wallCol.col
        solidWall.tex = wallTex
        width = 64
        height = 64
        blocks = Array(width * height) {
            val x = it % width
            val y = it / width
            
            val col = getCol(levelHeader.plane0?.get(it)?.toInt() ?: 0, levelHeader.plane1?.get(it)?.toInt() ?: 0)
            val block = getBlockFromPlane(levelHeader.plane0?.get(it)?.toInt() ?: 0, levelHeader.plane1?.get(it)?.toInt() ?: 0)
            block.id = 0
            block.col = col

            if (block.tex == -1) {
                block.tex = wallTex
            }
            if (block.floorTex == -1) {
                block.floorTex = floorTex
            }
            if (block.ceilTex == -1) {
                block.ceilTex = ceilTex
            }
            if (block.col == -1) {
                block.col = Art.getCol(wallCol)
            }
            if (block.floorCol == -1) {
                block.floorCol = Art.getCol(floorCol)
            }
            if (block.ceilCol == -1) {
                block.ceilCol = Art.getCol(ceilCol)
            }
            
            block.level = this
            block.x = x
            block.y = y
            
            block
        }

        for (y in 0 until h) {
            for (x in 0 until w) {
                val col = pixels[x + y * w] and 0xFFFFFF
                decorateBlock(x, y, blocks[x + y * w], col)
            }
        }
    }
    
    private fun getCol(plane0: Int, plane1: Int): Int {
        if (plane1 > 0) return 0xFFFFFF.col
        
        val gray = 0xC0C0C0.col
        val blue = 0x0000FF.col
        val yellow = 0xFFFF00.col
        val brown = 0x7F3300.col
        val cyan = 0x00FFFF.col
        val green = 0x00FF00.col
        val red = 0xFF0000.col
        val purple = 0xFF00FF.col
        return when (plane0) {
            1, 2, 27, 3, 4, 6, 28 -> gray
            24, 26 -> yellow
            5, 7, 8, 9, 41 -> blue
            12, 10, 11, 23 -> brown
            13 -> gray
            15, 14 -> cyan
            16 -> green
            17, 18, 20, 38 -> red
            19, 25 -> purple
            else -> gray
        }
    }
    
    private fun getBlockFromPlane(plane0: Int, plane1: Int): Block {
        return if (plane1 > 0) {
            Block()
        } else {
            when (plane0) {
                in 1..63 -> SolidBlock()
                in 90..101 -> DoorBlock()
                else -> Block()
            }
        }
    }
}