package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt

class WinBlock: Block(ModArt.walls, ModArt.floors) {
    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        if (entity is Player) {
            entity.win()
        }
    }
}