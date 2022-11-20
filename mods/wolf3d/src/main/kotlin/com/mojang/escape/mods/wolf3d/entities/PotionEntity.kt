package com.mojang.escape.mods.wolf3d.entities

import com.mojang.escape.col
import com.mojang.escape.entities.ItemEntity
import com.mojang.escape.entities.Player
import kotlin.random.Random

class PotionEntity(x: Double, z: Double): ItemEntity(x, z, 16 + 6, 0x00FF00.col) {
    override fun onPickup(player: Player) {
        player.health += Random.nextInt(4) + 1
    }
}