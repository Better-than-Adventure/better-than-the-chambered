package com.mojang.escape.level.physics

data class Point2<T: Number>(val x: T, val y: T) {
    constructor(pair: Pair<T, T>): this(pair.first, pair.second)

    fun toPair() = Pair(x, y)
    
    fun toPoint2D() = Point2D(x.toDouble(), y.toDouble())
    fun toPoint2I() = Point2I(x.toInt(), y.toInt())
}

typealias Point2D = Point2<Double>
typealias Point2I = Point2<Int>
