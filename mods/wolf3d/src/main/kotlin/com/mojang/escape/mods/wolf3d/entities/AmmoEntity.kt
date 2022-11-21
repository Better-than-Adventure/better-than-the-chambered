package com.mojang.escape.mods.wolf3d.entities

import com.mojang.escape.col
import com.mojang.escape.entities.ItemEntity
import com.mojang.escape.entities.Player
import com.mojang.escape.mods.wolf3d.ModArt
import kotlin.random.Random

class AmmoEntity(x: Double, z: Double): ItemEntity(x, z, 8 * 0 + 0, 0xFFFFFF.col, ModArt.sprites) {
    override fun onPickup(player: Player) {
        player.ammo += Random.nextInt(7) + 1
    }
}