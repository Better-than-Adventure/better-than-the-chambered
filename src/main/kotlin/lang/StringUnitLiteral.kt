package com.mojang.escape.lang

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap

class StringUnitLiteral(override val value: String, val lang: Language? = null) : IStringUnit {
    override fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int) {
        for (i in value.indices) {
            val ch = (lang ?: Game.lang).fontString.indexOf(value[i])
            if (ch < 0) continue

            val xx = ch % 42
            val yy = ch / 42
            bitmap.draw((lang ?: Game.lang).fontBitmap, x + i * 6, y, xx * 6, yy * 8, 5, 8, col)
        }
    }
}