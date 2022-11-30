package com.mojang.escape.mods.prelude.level.provider

import com.mojang.escape.GameSession
import com.mojang.escape.alpha
import com.mojang.escape.col
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.WallBlock
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.entities.BoulderEntity
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
            blocks = getLevelBlocks(name, img, default),
            entities = getLevelEntities(img)
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
            0x00FFFF -> VanishBlock(
                pos = pos,
                wallArt = ModArt.walls,
                wallTex = 8 * 0 + 1,
                wallCol = default.wallCol.col,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col,
            )
            0xFFFF64 -> ChestBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col,
                contents = Item.PowerGlove
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
            0xC1C14D -> LootBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
            0xC6C6C6 -> DoorBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col,
                doorArt = ModArt.walls,
                doorTex = 8 * 0 + 4,
                doorCol = 0xC6C6C6.col,
                triggerId = id
            )
            0x00FFA7 -> SwitchBlock(
                pos = pos,
                col = default.wallCol.col,
                triggerEmitId = id
            )
            0x009380 -> PressurePlateBlock(
                pos = pos,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col,
                triggerEmitId = id
            )
            0xFF0005, 0x3F3F60 -> IceBlock(
                pos = pos,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col
            )
            0xC6C697 -> LockedDoorBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol.col,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol.col,
                doorArt = ModArt.walls,
                doorTex = 8 * 0 + 5,
                doorCol = 0xC6C697.col,
                triggerId = id
            )
            0xFFBA02 -> AltarBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol,
                ceilArt = default.ceilArt,
                ceilTex = default.ceilTex,
                ceilCol = default.ceilCol
            )
            0x749327 -> SpiritWallBlock(
                pos = pos,
                floorArt = default.floorArt,
                floorTex = default.floorTex,
                floorCol = default.floorCol,
                ceilArt = default.floorArt,
                ceilTex = default.floorTex,
                ceilCol = default.ceilCol
            )
            
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
        val list = mutableListOf<Entity>()
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val pos = Point3D(x + 0.0, 0.0, y + 0.0)

                val col = image[x, y] and 0xFFFFFF
                val id = 255 - image[x, y].alpha

                val entity = getEntity(pos, col)
                if (entity != null) {
                    list.add(entity)
                }
            }
        }
        
        return list
    }
    
    private fun getEntity(pos: Point3D, col: Int): Entity? {
        return when (col) {
            0xAA5500 -> BoulderEntity(
                pos = pos,
                rot = 0.0,
                vel = Point3D(0.0, 0.0, 0.0),
                rotVel = 0.0
            )
            else -> null
        }
    }
}