package com.mojang.escape.entities

import com.mojang.escape.col
import kotlin.random.Random

class AmmoEntity(x: Double, z: Double): ItemEntity(x, z, 16 + 5, 0xFFFFFF.col) {
    override fun onPickup(player: Player) {
        player.ammo += Random.nextInt(7) + 1
    }
}