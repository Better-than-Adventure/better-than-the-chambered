package com.mojang.escape.mods.wolf3d.level.block

import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.wolf3d.ModArt

class TexturedBlock(wallTex: Int, floorTex: Int): Block(ModArt.walls, ModArt.floors) {
    init {
        this.tex = wallTex
        this.floorTex = floorTex
    }
}