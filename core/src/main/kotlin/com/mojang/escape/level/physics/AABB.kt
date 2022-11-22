package com.mojang.escape.level.physics

data class RelativeAABB
@Throws(IllegalArgumentException::class) constructor(val min: Point2D, val max: Point2D) {
    constructor(x0: Double, y0: Double, x1: Double, y1: Double): this(Point2D(x0, y0), Point2D(x1, y1))
    constructor(radius: Double): this(-radius, -radius, +radius, +radius)

    val dx = max.x - min.x
    val dy = max.y - min.y
    
    init {
        if (dx < 0 || dy < 0) {
            throw IllegalArgumentException("min > max!!")
        }
    }

    fun offset(offset: Point2D) = offset(offset.x, offset.y)
    fun offset(x: Double, y: Double) = AABB(min.x + x, min.y + y, max.x + x, max.y + y)
}

data class AABB 
@Throws(IllegalArgumentException::class) constructor(val min: Point2D, val max: Point2D) {
    constructor(x0: Double, y0: Double, x1: Double, y1: Double) : this(Point2D(x0, y0), Point2D(x1, y1))

    val dx = max.x - min.x
    val dy = max.y - min.y

    init {
        if (dx < 0 || dy < 0) {
            throw IllegalArgumentException()
        }
    }
    
    fun contains(point: Point2<*>): Boolean {
        val point2D = point.toPoint2D()
        return point2D.x in min.x..max.x && point2D.y in min.y..max.y
    }

    fun intersects(other: AABB) = this.contains(other.min) || this.contains(other.max)
}