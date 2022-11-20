package com.mojang.escape.lang

import com.mojang.escape.gui.Bitmap

interface StringUnit {
    val value: String

    val length: Int
        get() = value.length

    operator fun plus(other: StringUnit): StringUnitGroup {
        return StringUnitGroup(this, other)
    }

    fun format(vararg params: StringUnit): StringUnitFormatted {
        return StringUnitFormatted(this, *params)
    }

    fun draw(bitmap: Bitmap, x: Int, y: Int, col: Int)
}