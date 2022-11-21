package com.mojang.escape.mods.wolf3d

import com.mojang.escape.Art

object ModArt {
    val floors = Art.loadBitmap("/tex/wolf3d_floors.png", this::class.java)
    val sprites = Art.loadBitmap("/tex/wolf3d_sprites.png", this::class.java)
    val walls = Art.loadBitmap("/tex/wolf3d_walls.png", this::class.java)
}