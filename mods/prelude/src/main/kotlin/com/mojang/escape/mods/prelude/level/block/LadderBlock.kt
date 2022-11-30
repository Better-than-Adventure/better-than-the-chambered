package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.col
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.ILevelChangeBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class LadderBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int,
    override val levelChangeIdOut: Int,
    override val levelChangeIdIn: Int,
    override val targetLevel: String,
    down: Boolean
): SpriteEmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = if (down) 1 else floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = if (!down) 1 else ceilTex,
    ceilCol = ceilCol,
    spriteArt = ModArt.sprites,
    spriteTex = if (down) 8 * 1 + 3 else 8 * 1 + 4,
    spriteCol = 0xDB8E53.col
), ILevelChangeBlock {
    override fun onEntityEnter(level: Level, entity: Entity) {
        if (entity is Player && !entity.inLevelChangeBlock) {
            entity.inLevelChangeBlock = true
            level.switchLevel(this)
        }
    }

    override fun onEntityLeave(level: Level, entity: Entity) {
        if (entity is Player) {
            entity.inLevelChangeBlock = false
        }
    }
    
    override fun onLevelEnter(level: Level, entity: Entity) {
        ModSound.ladder.play()
    }

    override fun onLevelLeave(level: Level, entity: Entity) {
        // Do nothing
    }
}