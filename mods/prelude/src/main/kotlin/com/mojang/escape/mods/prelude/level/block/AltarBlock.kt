package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.col
import com.mojang.escape.entities.Entity
import com.mojang.escape.mods.prelude.entities.GhostBossEntity
import com.mojang.escape.mods.prelude.entities.GhostEntity
import com.mojang.escape.entities.KeyEntity
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.mods.prelude.gui.RubbleSprite
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class AltarBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): SpriteEmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol,
    spriteArt = ModArt.sprites,
    spriteTex = 8 * 2 + 4,
    spriteCol = 0xE2FFE4.col
), ICollidableBlock {
    override val collisionBox = RelativeAABB(0.5)
    
    override fun onEntityCollision(level: Level, entity: Entity) {
        if (entity is GhostEntity || entity is GhostBossEntity) {
            entity.removed = true
            
            for (i in 0 until 8) {
                val sprite = RubbleSprite()
                sprite.col = this.sprite.col
                level.sprites.add(sprite)
            }
            
            if (entity is GhostBossEntity) {
                level.entities.add(KeyEntity(
                    Point3D(pos.x + 0.5, 0.5, pos.z + 0.5), 
                    0.0, 
                    Point3D(0.0, 0.0, 0.0),
                    0.0
                ))
                ModSound.bosskill.play()
            } else {
                Sound.altar.play()
            }
        }
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }
}