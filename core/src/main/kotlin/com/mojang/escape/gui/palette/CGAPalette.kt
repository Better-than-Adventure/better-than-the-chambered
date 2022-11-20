package com.mojang.escape.gui.palette

class CGAPalette: Palette() {
    override val values: Array<Triple<Int, Int, Int>> = arrayOf(
        Triple(0x00, 0x00, 0x00), // Black
        Triple(0x55, 0xFF, 0xFF), // Cyan
        Triple(0xFF, 0x55, 0xFF), // Magenta
        Triple(0xFF, 0xFF, 0xFF) // White
    )
}