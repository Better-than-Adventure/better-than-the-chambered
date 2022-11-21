package com.mojang.escape.mods.wolf3d.level.block

import com.mojang.escape.level.block.SolidBlock
import com.mojang.escape.mods.wolf3d.ModArt

open class TexturedSolidBlock(wallTex: Int, floorTex: Int): SolidBlock(ModArt.walls, ModArt.floors) {
    init {
        this.tex = wallTex
        this.floorTex = floorTex
    }
}