package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.level.block.Block

class PressurePlateBlock: Block() {
    private var pressed = false

    init {
        floorTex = 2
    }

    override fun tick() {
        super.tick()
        val r = 0.2
        val steppedOn = level!!.containsBlockingNonFlyingEntity(x - r, y - r, x + r, y + r)
        if (steppedOn != pressed) {
            pressed = steppedOn
            floorTex = when (pressed) {
                true -> 3
                false -> 2
            }
            level!!.trigger(id, pressed)
            when (pressed) {
                true -> Sound.click1
                false -> Sound.click2
            }.play()
        }
    }

    override fun getFloorHeight(e: Entity): Double {
        return when (pressed) {
            true -> -0.02
            false -> 0.02
        }
    }
}