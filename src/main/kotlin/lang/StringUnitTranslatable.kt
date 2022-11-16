package com.mojang.escape.lang

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap

class StringUnitTranslatable(private val key: String, val lang: Language? = null): IStringUnit {
    override val value: String
        get() {
            return (lang ?: Game.lang).strings.getProperty(key) ?: key
        }

    override fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int) {
        val str = value
        for (i in str.indices) {
            val ch = (lang ?: Game.lang).fontString.indexOf(str[i])
            if (ch < 0) continue

            val xx = ch % 42
            val yy = ch / 42
            bitmap.draw((lang ?: Game.lang).fontBitmap, x + i * 6, y, xx * 6, yy * 8, 5, 8, col)
        }
    }
}