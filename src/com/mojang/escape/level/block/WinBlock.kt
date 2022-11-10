package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player

class WinBlock: Block() {
    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        if (entity is Player) {
            entity.win()
        }
    }
}