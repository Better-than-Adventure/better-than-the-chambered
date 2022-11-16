package com.mojang.escape.lang

import com.mojang.escape.gui.Bitmap
import com.mojang.escape.toLiteral

class StringUnitFormatted(private val stringUnit: IStringUnit, private vararg val params: IStringUnit): IStringUnit {
    override val value: String
        get() {
            var str = stringUnit.value
            for (param in params) {
                str = str.replaceFirst("%s", param.value)
            }
            return str
        }

    override fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int) {
        val str = value.split("%s")

        str[0].toLiteral().draw(bitmap, x, y, col)
        var charsWritten = str[0].length
        for (i in 1 until str.size) {
            params[i - 1].draw(bitmap, x + charsWritten * 6, y, col)
            charsWritten += params[i - 1].length
            str[i].toLiteral().draw(bitmap, x + charsWritten * 6, y, col)
            charsWritten += str[i].length
        }
    }
}