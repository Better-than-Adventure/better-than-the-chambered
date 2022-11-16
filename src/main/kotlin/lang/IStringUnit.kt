package com.mojang.escape.lang

import com.mojang.escape.gui.Bitmap

interface IStringUnit {
    val value: String

    val length: Int
        get() = value.length

    operator fun plus(other: IStringUnit): StringUnitGroup {
        return StringUnitGroup(this, other)
    }

    fun format(vararg params: IStringUnit): StringUnitFormatted {
        return StringUnitFormatted(this, *params)
    }

    fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int)
}