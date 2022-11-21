package com.mojang.escape.mods.wolf3d.entities

import com.mojang.escape.col
import com.mojang.escape.entities.ItemEntity
import com.mojang.escape.entities.Player
import com.mojang.escape.mods.wolf3d.ModArt
import kotlin.random.Random

class PotionEntity(x: Double, z: Double): ItemEntity(x, z, 8 * 0 + 1, 0x00FF00.col, ModArt.sprites) {
    override fun onPickup(player: Player) {
        player.health += Random.nextInt(4) + 1
    }
}