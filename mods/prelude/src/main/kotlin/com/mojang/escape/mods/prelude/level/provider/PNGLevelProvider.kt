package com.mojang.escape.mods.prelude.level.provider

import com.mojang.escape.GameSession
import com.mojang.escape.alpha
import com.mojang.escape.col
import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.WallBlock
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.level.PNGLevel
import com.mojang.escape.mods.prelude.level.block.*
import com.mojang.escape.toTranslatable
import com.mojang.escape.util.Image
import java.lang.RuntimeException

class PNGLevelProvider: ILevelProvider {
    private data class BlockDefaults(
        val wallArt: Bitmap = ModArt.walls,
        val wallTex: Int = 0,
        val wallCol: Int = 0xB3CEE2,
        val floorArt: Bitmap = ModArt.floors,
        val floorTex: Int = 0,
        val floorCol: Int = 0x9CA09B,
        val ceilArt: Bitmap = ModArt.floors,
        val ceilTex: Int = 0,
        val ceilCol: Int = 0x9CA09B
    )
    private val defaults = mapOf(
        "crypt" to BlockDefaults(
            floorCol = 0x404040,
            ceilCol = 0x404040,
            wallCol = 0x404040
        ),
        "dungeon" to BlockDefaults(
            wallCol = 0xC64954,
            floorCol = 0x8E4A51,
            ceilCol = 0x8E4A51
        ),
        "ice" to BlockDefaults(
            floorCol = 0xB8DBE0,
            ceilCol = 0xB8DBE0,
            wallCol = 0x6BE8FF
        ),
        "overworld" to BlockDefaults(
            ceilTex = -1,
            floorCol = 0x508253,
            floorTex = 8 + 3,
            wallCol = 0xA0A0A0
        ),
        "temple" to BlockDefaults(
            floorCol = 0x8A6496,
            ceilCol = 0x8A6496,
            wallCol = 0xCFADD8
        )
    )
    
    override fun getFirstLevel(session: GameSession): Level {
        return getLevelByName(session, "start")!!
    }

    override fun getLevelByName(session: GameSession, name: String): Level? {
        val img = try {
            Image.read("/level/$name.png", this::class.java)
        } catch (e: Exception) {
            return null
        }
        val default = defaults[name] ?: BlockDefaults()
        
        val level = PNGLevel(
            session = session,
            name = "level.$name.name".toTranslatable(),
            lengthX = img.width,
            lengthZ = img.height,
            spawn = Point2I(26, 27),
            getLevelBlocks(name, img, default),
            getLevelEntities(img)
        )
        
        level.blocks.forEach { 
            it.onInit(level)
        }
        
        return level
    }
    
    private fun getLevelBlocks(levelName: String, image: Image, default: BlockDefaults): Array<Block> {
        val blocks = Array(image.width * image.height) {
            val x = it % image.width
            val y = it / image.width
            
            val col = image[x, y] and 0xFFFFFF
            val id = 255 - image[x, y].alpha
            
            getBlock(levelName, id, Point2I(x, y), col, default)
        }
        
        return blocks
    }
    
    private fun getBlock(levelName: String, id: Int, pos: Point2I, col: Int, default: BlockDefaults): Block {
        return when (col) {
            0x93FF9B -> WallBlock(
                pos = pos,
                art = default.wallArt,
                tex = 8,
                col = 0x2AAF33.col
            )
            0x009300 -> PitBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
            0xFFFFFF -> WallBlock(
                pos = pos,
                art = default.wallArt,
                tex = default.wallTex,
                col = default.wallCol.col
            )
            0x0000FF -> WaterBlock(
                pos = pos,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
            0xFF3A02 -> TorchBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
            0x4C4C4C -> BarsBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
            0xFF66FF, 0x9E009E -> LadderBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col,
                levelChangeIdOut = when (levelName) {
                    "crypt" -> when (id) {
                        1 -> 2
                        else -> -1
                    }
                    "dungeon" -> when (id) {
                        1 -> 2
                        else -> -1
                    }
                    "ice" -> when (id) {
                        1 -> 5
                        else -> -1
                    }
                    "overworld" -> when (id) {
                        1 -> 1
                        2 -> 1
                        3 -> 1
                        5 -> 1
                        else -> -1
                    }
                    "start" -> when (id) {
                        1 -> 1
                        2 -> 1
                        else -> -1
                    }
                    "temple" -> when (id) {
                        1 -> 3
                        else -> -1
                    }
                    else -> -1
                },
                levelChangeIdIn = id,
                down = col == 0x9E009E,
                targetLevel = when (levelName) {
                    "crypt" -> when (id) {
                        1 -> "overworld"
                        else -> ""
                    }
                    "dungeon" -> when (id) {
                        1 -> "start"
                        else -> ""
                    }
                    "ice" -> when (id) {
                        1 -> "overworld"
                        else -> ""
                    }
                    "overworld" -> when (id) {
                        1 -> "start"
                        2 -> "crypt"
                        3 -> "temple"
                        5 -> "ice"
                        else -> ""
                    }
                    "start" -> when (id) {
                        1 -> "overworld"
                        2 -> "dungeon"
                        else -> ""
                    }
                    "temple" -> when (id) {
                        1 -> "overworld"
                        else -> ""
                    }
                    else -> ""
                }
            )
            // TODO
            else -> EmptyBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
        }
    }
    
    private fun getLevelEntities(image: Image): MutableList<Entity> {
        return mutableListOf()
    }
}