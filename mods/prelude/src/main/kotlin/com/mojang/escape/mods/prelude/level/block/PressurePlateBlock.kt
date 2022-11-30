package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.IFlyingEntity
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.ITriggerEmitterBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.mods.prelude.ModArt

class PressurePlateBlock(
    pos: Point2I,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int,
    override val triggerEmitId: Int
): EmptyBlock(
    pos = pos,
    floorArt = ModArt.floors,
    floorTex = 8 * 0 + 2,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
), ITriggerEmitterBlock {
    private var pressed: Boolean = false
    
    override fun onEntityEnter(level: Level, entity: Entity) {
        if (!pressed) {
            if (entity !is IFlyingEntity || !entity.flying) {
                Sound.click1.play()
                pressed = true
                level.trigger(entity, triggerEmitId, 1)
                floorTex = 8 * 0 + 3
            }
        }
    }

    override fun onEntityLeave(level: Level, entity: Entity) {
        if (pressed) {
            if (entitiesInBlock(level).isEmpty()) {
                Sound.click2.play()
                pressed = false
                level.trigger(entity, triggerEmitId, 0)
                floorTex = 8 * 0 + 2
            }
        }
    }

    override fun getFloorHeight(level: Level, entity: Entity): Double {
        return when (pressed) {
            true -> -0.02
            false -> 0.02
        }
    }
}