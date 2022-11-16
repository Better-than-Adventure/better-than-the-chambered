package com.mojang.escape

import com.mojang.escape.lang.ITranslatable
import com.mojang.escape.lang.Translatable
import com.mojang.escape.lang.TranslatableGroup
import kotlin.math.abs

val String.displayWidth: Int
get() {
    return this.length * 6
}

val Int.col: Int
get() {
    return Art.getCol(this)
}

fun Int.closest(a: Int, b: Int): Int {
    val da = abs(a - this)
    val db = abs(b - this)

    if (da < db) return a
    return b
}

inline val String.indent: Int
get() {
    var i = 0
    for (c in this) {
        if (c == ' ') {
            i++
        } else break
    }
    return i
}

inline val String.translatable: Translatable
get() = Translatable(this)

infix fun String.and(other: ITranslatable): TranslatableGroup {
    return TranslatableGroup(this, other)
}