package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.level.block.Block

class WinBlock: Block() {
    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        if (entity is Player) {
            entity.win()
        }
    }
}