package com.mojang.escape.lang

import com.mojang.escape.Game
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.menu.settings.GameSettings

class StringUnitTranslatable(private val key: String, val lang: Language? = null): IStringUnit {
    override val value: String
        get() {
            return (lang ?: GameSettings.language.value).strings.getProperty(key) ?: key
        }

    override fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int) {
        for (i in value.indices) {
            var chX = -1
            var chY = -1
            for (line in (lang ?: GameSettings.language.value).fontStringLines.withIndex()) {
                val pos = line.value.indexOf(value[i])
                if (pos != -1) {
                    chX = pos
                    chY = line.index
                }
            }
            if (chX == -1 || chY == -1) continue

            bitmap.draw((lang ?: GameSettings.language.value).fontBitmap, x + i * 6, y, chX * 6, chY * 8, 5, 8, col)
        }
    }
}