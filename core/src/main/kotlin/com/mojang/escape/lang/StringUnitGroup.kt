package com.mojang.escape.lang

import com.mojang.escape.gui.Bitmap

class StringUnitGroup(vararg params: StringUnit): StringUnit {
    private val parts = mutableListOf<StringUnit>()

    override val value: String
        get() {
            return parts.joinToString("") { it.value }
        }

    init {
        for (param in params) {
            if (param is StringUnitGroup) {
                parts.addAll(param.parts)
            } else {
                parts.add(param)
            }
        }
    }

    override fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int) {
        var charsWritten = 0
        for (part in parts) {
            part.draw(bitmap, x + charsWritten * 6, y, col)
            charsWritten += part.length
        }
    }
}