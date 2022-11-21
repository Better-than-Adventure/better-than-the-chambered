package com.mojang.escape.level.block

import com.mojang.escape.gui.Bitmap

open class SolidBlock(wallArt: Bitmap, floorArt: Bitmap): Block(wallArt, floorArt) {
    init {
        solidRender = true
        blocksMotion = true
    }
}