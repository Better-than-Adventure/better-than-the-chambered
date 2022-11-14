package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player

class WaterBlock: Block() {
    private var steps = 0

    init {
        blocksMotion = true
    }

    override fun tick() {
        super.tick()
        steps--
        if (steps <= 0) {
            floorTex = 8 + random.nextInt(3)
            floorCol = Art.getCol(0x0000FF)
            steps = 16
        }
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is Player) {
            if (entity.selectedItem == Item.Flippers) {
                return false
            }
        }
        if (entity is Bullet) {
            return false
        }
        return blocksMotion
    }

    override fun getFloorHeight(e: Entity): Double {
        return -0.5
    }

    override fun getWalkSpeed(player: Player): Double {
        return 0.4
    }
}