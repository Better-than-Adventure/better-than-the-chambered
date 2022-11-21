package com.mojang.escape.mods.prelude.level.provider

import com.mojang.escape.*
import com.mojang.escape.entities.*
import com.mojang.escape.mods.prelude.entities.*
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.*
import com.mojang.escape.mods.prelude.level.block.*
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.util.Image
import java.lang.Exception
import java.lang.RuntimeException

class PNGLevelProvider(
    private val name: String, 
    private val wallCol: Int = 0xB3CEE2, 
    private val floorCol: Int = 0x9CA09B, 
    private val ceilCol: Int = 0x9CA09B, 
    private val wallTex: Int = 0, 
    private val floorTex: Int = 0, 
    private val ceilTex: Int = 0
): ILevelProvider {
    private val width: Int
    private val height: Int
    private val pixels: IntArray
    
    init {
        try {
            val img = Image.read("/level/$name.png", this::class.java)
            
            width = img.width
            height = img.height
            pixels = IntArray(width * height)
            img.getRGB(pixels)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
    
    override fun getName(): StringUnit {
        return "level.$name.name".toTranslatable()
    }

    override fun getWallCol(): Int {
        return wallCol
    }

    override fun getWallTex(): Int {
        return wallTex
    }

    override fun getFloorCol(): Int {
        return floorCol
    }

    override fun getFloorTex(): Int {
        return floorTex
    }

    override fun getCeilCol(): Int {
        return ceilCol
    }

    override fun getCeilTex(): Int {
        return ceilTex
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getBlocks(level: Level): Array<Block> {
        val blocks = Array(width * height) {
            val x = it % width
            val y = it / width
            
            val col = pixels[x + y * width] and 0xFFFFFF
            val id = 255 - pixels[x + y * width].alpha

            val block = getBlock(col)
            block.id = id

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

            block.level = level
            block.x = x
            block.y = y
            
            // Nasty little edge case decorations
            decorateBlock(x, y, block, col)
            
            block
        }
        for (i in blocks.withIndex()) {
            i.value.decorate(blocks, width, height, i.index % width, i.index / width)
        }
        return blocks
    }

    override fun getEntities(level: Level): MutableList<Entity> {
        val entities = mutableListOf<Entity>()
        
        for (i in pixels.withIndex()) {
            val x = i.index % width
            val y = i.index / width
            
            val col = i.value and 0xFFFFFF
            
            val entity: Entity? = when (col) {
                0xAA5500 -> BoulderEntity(x, y)
                0xFF0000 -> BatEntity(x.toDouble(), y.toDouble())
                0xFF0001 -> BatBossEntity(x.toDouble(), y.toDouble())
                0xFF0002 -> OgreEntity(x.toDouble(), y.toDouble())
                0xFF0003 -> OgreBossEntity(x.toDouble(), y.toDouble())
                0xFF0004 -> EyeEntity(x.toDouble(), y.toDouble())
                0xFF0005 -> EyeBossEntity(x.toDouble(), y.toDouble())
                0xFF0006 -> GhostEntity(x.toDouble(), y.toDouble())
                0xFF0007 -> GhostBossEntity(x.toDouble(), y.toDouble())
                else -> null
            }
            
            if (entity != null) {
                entities.add(entity)
                entity.level = level
                entity.updatePos()
            }
        }
        
        return entities
    }
    
    override fun getSpawn(level: Level): Pair<Int, Int>? {
        for (i in pixels.withIndex()) {
            if ((i.value and 0xFFFFFF) == 0xFFFF00) {
                return Pair(i.index % width, i.index / width)
            }
        }
        
        return null
    }
    
    private fun getBlock(col: Int): Block {
        return when (col) {
            0x93FF9B -> SolidBlock(ModArt.walls, ModArt.floors)
            0x009300 -> PitBlock()
            0xFFFFFF -> SolidBlock(ModArt.walls, ModArt.floors)
            0x00FFFF -> VanishBlock()
            0xFFFF64 -> ChestBlock()
            0x0000FF -> WaterBlock()
            0xFF3A02 -> TorchBlock()
            0x4C4C4C -> BarsBlock()
            0xFF66FF -> LadderBlock(false)
            0x9E009E -> LadderBlock(true)
            0xC1C14D -> LootBlock()
            0xC6C6C6 -> DoorBlock()
            0x00FFA7 -> SwitchBlock()
            0x009380 -> PressurePlateBlock()
            0xFF0005 -> IceBlock()
            0x3F3F60 -> IceBlock()
            0xC6C697 -> LockedDoorBlock()
            0xFFBA02 -> AltarBlock()
            0x749327 -> SpiritWallBlock()
            0x1A2108 -> Block(ModArt.walls, ModArt.floors)
            0x00C2A7 -> FinalUnlockBlock()
            0x000056 -> WinBlock()
            else -> Block(ModArt.walls, ModArt.floors)
        }
    }
    
    private fun decorateBlock(x: Int, y: Int, block: Block, col: Int) {
        when (col) {
            0x1A2108, 0xFF0007 -> {
                block.floorTex = 7
                block.ceilTex = 7
            }
            0xC6C6C6, 0xC6C697 -> {
                block.col = 0xA0A0A0.col
            }
            0x653A00 -> {
                block.floorCol = 0xB56600.col
                block.floorTex = 3 * 8 + 1
            }
            0x93FF9B -> {
                block.col = 0x2AAF33.col
                block.tex = 8
            }
        }
    }
}