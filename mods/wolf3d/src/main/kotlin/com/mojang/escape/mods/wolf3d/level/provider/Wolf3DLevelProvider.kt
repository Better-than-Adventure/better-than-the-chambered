package com.mojang.escape.mods.wolf3d.level.provider

import com.mojang.escape.Art
import com.mojang.escape.col
import com.mojang.escape.mods.wolf3d.entities.AmmoEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.mods.wolf3d.entities.GuardEntity
import com.mojang.escape.mods.wolf3d.entities.PotionEntity
import com.mojang.escape.mods.wolf3d.entities.SSEntity
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.*
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.mods.wolf3d.data.GameMaps
import com.mojang.escape.toLiteral

class Wolf3DLevelProvider(val levelHeader: GameMaps.LevelHeader): ILevelProvider {
    override fun getName(): StringUnit {
        return levelHeader.name.toLiteral()
    }

    override fun getWallCol(): Int {
        return 0xFFFFFF.col
    }

    override fun getWallTex(): Int {
        return 0
    }

    override fun getFloorCol(): Int {
        return 0x707070
    }

    override fun getFloorTex(): Int {
        return 0
    }

    override fun getCeilCol(): Int {
        return 0x383838
    }

    override fun getCeilTex(): Int {
        return 0
    }

    override fun getWidth(): Int {
        return levelHeader.width
    }

    override fun getHeight(): Int {
        return levelHeader.height
    }

    override fun getBlocks(level: Level): Array<Block> {
        val blocks = Array(levelHeader.width * levelHeader.height) {
            val x = it % levelHeader.width
            val y = it / levelHeader.width

            val plane0 = levelHeader.plane0?.get(it)?.toInt() ?: 0
            val plane1 = levelHeader.plane1?.get(it)?.toInt() ?: 0
            val plane2 = levelHeader.plane2?.get(it)?.toInt() ?: 0
            
            val block = getBlock(plane0, plane1, plane2)
            block.col = getCol(plane0, plane1, plane2)

            if (block.tex == -1) {
                block.tex = getWallTex()
            }
            if (block.floorTex == -1) {
                block.floorTex = getFloorTex()
            }
            if (block.ceilTex == -1) {
                block.ceilTex = getCeilTex()
            }
            if (block.col == -1) {
                block.col = Art.getCol(getWallCol())
            }
            if (block.floorCol == -1) {
                block.floorCol = Art.getCol(getFloorCol())
            }
            if (block.ceilCol == -1) {
                block.ceilCol = Art.getCol(getCeilCol())
            }

            block.level = level
            block.x = x
            block.y = y

            block
        }
        for (i in blocks.withIndex()) {
            i.value.decorate(blocks, levelHeader.width, levelHeader.height, i.index % levelHeader.width, i.index / levelHeader.width)
        }
        return blocks
    }

    override fun getEntities(level: Level): MutableList<Entity> {
        val blocks = getBlocks(level)
        val entities = mutableListOf<Entity>()
        for (i in blocks.indices) {
            val x = i % level.width
            val y = i / level.width
            val e = getEntity(levelHeader.plane1!![i].toInt(), x, y)
            if (e != null) {
                e.level = level
                e.updatePos()
                entities += e
            }
        }
        
        return entities
    }

    override fun getSpawn(level: Level): Pair<Int, Int>? {
        val blocks = getBlocks(level)
        for (i in blocks.indices) {
            val x = i % level.width
            val y = i / level.width
            val id = levelHeader.plane1!![i].toInt()
            if (id == 19 || id == 20 || id == 21 || id == 22) {
                return Pair(x, y)
            }
        }
        return null
    }
    
    private fun getBlock(plane0: Int, plane1: Int, plane2: Int): Block {
        return if (plane1 > 0) {
            when (plane1) {
                //30 -> BarsBlock()
                //in 52..55 -> LootBlock()
                //98 -> VanishBlock()
                else -> Block()
            }
        } else {
            when (plane0) {
                //21 -> LadderBlock(true)
                in 1..63 -> SolidBlock()
                //in 90..101 -> DoorBlock()
                else -> Block()
            }
        }
    }

    private fun getCol(plane0: Int, plane1: Int, plane2: Int): Int {
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
    
    private fun getEntity(plane1: Int, x: Int, z: Int): Entity? {
        return when (plane1) {
            // Guard
            in 180..183 -> GuardEntity(x.toDouble(), z.toDouble())
            in 144..147 -> GuardEntity(x.toDouble(), z.toDouble())
            in 108..111 -> GuardEntity(x.toDouble(), z.toDouble())
            in 184..187 -> GuardEntity(x.toDouble(), z.toDouble())
            in 148..151 -> GuardEntity(x.toDouble(), z.toDouble())
            in 112..115 -> GuardEntity(x.toDouble(), z.toDouble())
            // SS
            in 198..201 -> SSEntity(x.toDouble(), z.toDouble())
            in 162..165 -> SSEntity(x.toDouble(), z.toDouble())
            in 126..129 -> SSEntity(x.toDouble(), z.toDouble())
            in 202..205 -> SSEntity(x.toDouble(), z.toDouble())
            in 166..169 -> SSEntity(x.toDouble(), z.toDouble())
            in 130..133 -> SSEntity(x.toDouble(), z.toDouble())
            // Items
            in 47..48 -> PotionEntity(x.toDouble(), z.toDouble())
            in 49..51 -> AmmoEntity(x.toDouble(), z.toDouble())
            else -> null
        }
    }
}