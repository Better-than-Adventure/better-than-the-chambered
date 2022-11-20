package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Sprite

class KeyEntity(x: Double, z: Double): ItemEntity(x, z, 16 + 3, 0x00FFFF) {
    override fun onPickup(player: Player) {
        player.keys++
    }
}