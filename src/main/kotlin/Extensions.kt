package com.mojang.escape

import com.mojang.escape.lang.*
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
fun String.toLiteral(lang: Language? = null): StringUnitLiteral {
    return StringUnitLiteral(this, lang)
}
fun String.toTranslatable(lang: Language? = null): StringUnitTranslatable {
    return StringUnitTranslatable(this, lang)
}

val Int.alpha: Int 
    get() {
        return (this shr 24) and 0xFF
    }

val Int.red: Int
    get() {
        return (this shr 16) and 0xFF
    }

val Int.green: Int 
    get() {
        return (this shr 8) and 0xFF
    }

val Int.blue: Int
    get() {
        return (this shr 0) and 0xFF
    }

fun Int.Companion.fromARGB(a: Int, r: Int, g: Int, b: Int): Int {
    return  (a.toUByte().toInt() shl 24) or 
            (r.toUByte().toInt() shl 16) or
            (g.toUByte().toInt() shl 8 ) or
            (b.toUByte().toInt() shl 0 )
}

fun Int.Companion.fromRGB(r: Int, g: Int, b: Int): Int {
    return fromARGB(0xFF, r, g, b)
}

@OptIn(ExperimentalUnsignedTypes::class)
infix fun UByteArray.equalsTo(other: UByteArray): Boolean {
    if (size != other.size) return false
    for (i in indices) {
        if (get(i) != other[i]) return false
    }

    return true
}

val <F, S> Pair<F, S>.x: F
    get() = first

val <F, S> Pair<F, S>.y: S
    get() = second

val <F, S, T> Triple<F, S, T>.x: F
    get() = first

val <F, S, T> Triple<F, S, T>.y: S
    get() = second

val <F, S, T> Triple<F, S, T>.z: T
    get() = third

val <F, S, T> Triple<F, S, T>.red: F
    get() = first

val <F, S, T> Triple<F, S, T>.green: S
    get() = second

val <F, S, T> Triple<F, S, T>.blue: T
    get() = third