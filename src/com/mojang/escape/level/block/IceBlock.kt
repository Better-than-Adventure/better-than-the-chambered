package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.entities.*

class IceBlock: Block() {
    init {
        blocksMotion = false
        floorTex = 16
    }

    override fun tick() {
        super.tick()
        floorCol = Art.getCol(0x8080FF)
    }

    override fun getWalkSpeed(player: Player): Double {
        if (player.selectedItem == Item.Skates) return 0.05
        return 1.4
    }

    override fun getFriction(player: Player): Double {
        if (player.selectedItem == Item.Skates) return 0.98
        return 1.0
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is Player) {
            return false
        }
        if (entity is Bullet) {
            return false
        }
        if (entity is EyeBossEntity) {
            return false
        }
        if (entity is EyeEntity) {
            return false
        }
        return true
    }
}