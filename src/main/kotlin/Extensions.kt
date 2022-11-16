package com.mojang.escape

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