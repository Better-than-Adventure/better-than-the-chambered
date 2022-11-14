package com.mojang.escape

import kotlin.math.ln
import kotlin.math.log
import kotlin.math.sqrt
import kotlin.random.Random

val String.displayWidth: Int
get() {
    return this.length * 6
}

val Int.col: Int
get() {
    return Art.getCol(this)
}

var nextNextGaussian: Double = 0.0
var haveNextNextGaussian: Boolean = false
fun Random.Default.nextGaussian(): Double {
    if (haveNextNextGaussian) {
        haveNextNextGaussian = false
        return nextNextGaussian
    } else {
        var v1: Double = 0.0
        var v2: Double = 0.0
        var s: Double = 0.0
        do {
            v1 = 2 * nextDouble() - 1 // between -1.0 and 1.0
            v2 = 2 * nextDouble() - 1 // between -1.0 and 1.0
            s = v1 * v1 + v2 * v2
        } while (s >= 1 || s == 0.0)
        val multiplier = sqrt(-2 * ln(s) / s)
        nextNextGaussian = v2 * multiplier
        haveNextNextGaussian = true
        return v1 * multiplier
    }
}

fun DoubleArray.setAll(value: Double) {
    for (i in this.indices) {
        this[i] = value
    }
}