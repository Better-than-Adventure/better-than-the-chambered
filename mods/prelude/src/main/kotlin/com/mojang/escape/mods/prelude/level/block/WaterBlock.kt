package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class WaterBlock: Block(ModArt.walls, ModArt.floors) {
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

    override fun onPlayerEnter(player: Player) {
        ModSound.splash.play()
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