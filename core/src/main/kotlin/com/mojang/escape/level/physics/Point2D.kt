package com.mojang.escape.level.physics

import kotlin.math.pow
import kotlin.math.sqrt

data class Point2<T: Number>(val x: T, val z: T) {
    constructor(pair: Pair<T, T>): this(pair.first, pair.second)

    fun toPair() = Pair(x, z)
    
    fun toPoint2D() = Point2D(x.toDouble(), z.toDouble())
    fun toPoint2I() = Point2I(x.toInt(), z.toInt())
    
    fun distanceToSquared(other: Point2<T>): Double {
        val thisD = this.toPoint2D()
        val otherD = other.toPoint2D()
        val dx = thisD.x - otherD.x
        val dz = thisD.z - otherD.z
        
        return dx.pow(2) + dz.pow(2)
    }
    
    fun distanceTo(other: Point2<T>) = sqrt(distanceToSquared(other))
}

typealias Point2D = Point2<Double>
typealias Point2I = Point2<Int>

data class Point3<T: Number>(val x: T, val y: T, val z: T) {
    constructor(triple: Triple<T, T, T>): this(triple.first, triple.second, triple.third)
    
    fun toTriple() = Triple(x, y, z)
    
    fun toPoint3D() = Point3D(x.toDouble(), y.toDouble(), z.toDouble())
    fun toPoint2D() = Point2D(x.toDouble(), z.toDouble())
}

typealias Point3D = Point3<Double>