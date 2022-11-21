package com.mojang.escape.mods.prelude

import com.mojang.escape.Art

object ModArt {
    val floors = Art.loadBitmap("/tex/prelude_floors.png", this::class.java)
    val sprites = Art.loadBitmap("/tex/prelude_sprites.png", this::class.java)
    val walls = Art.loadBitmap("/tex/prelude_walls.png", this::class.java)
}