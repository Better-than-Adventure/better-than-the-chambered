package com.mojang.escape.lang

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap

class StringUnitLiteral(override val value: String, val lang: Language? = null) : IStringUnit {
    override fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int) {
        for (i in value.indices) {
            var chX = -1
            var chY = -1
            for (line in (lang ?: Game.lang).fontStringLines.withIndex()) {
                val pos = line.value.indexOf(value[i])
                if (pos != -1) {
                    chX = pos
                    chY = line.index
                }
            }
            if (chX == -1 || chY == -1) continue

            bitmap.draw((lang ?: Game.lang).fontBitmap, x + i * 6, y, chX * 6, chY * 8, 5, 8, col)
        }
    }
}