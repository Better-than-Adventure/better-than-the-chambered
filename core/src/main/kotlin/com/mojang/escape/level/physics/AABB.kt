package com.mojang.escape.level.physics

data class RelativeAABB
@Throws(IllegalArgumentException::class) constructor(val min: Point2D, val max: Point2D) {
    constructor(x0: Double, z0: Double, x1: Double, z1: Double): this(Point2D(x0, z0), Point2D(x1, z1))
    constructor(radius: Double): this(-radius, -radius, +radius, +radius)

    val dx = max.x - min.x
    val dz = max.z - min.z
    
    init {
        if (dx < 0 || dz < 0) {
            throw IllegalArgumentException("min > max!!")
        }
    }

    fun offset(offset: Point2D) = offset(offset.x, offset.z)
    fun offset(x: Double, z: Double) = AABB(min.x + x, min.z + z, max.x + x, max.z + z)
}

data class AABB 
@Throws(IllegalArgumentException::class) constructor(val min: Point2D, val max: Point2D) {
    constructor(x0: Double, z0: Double, x1: Double, z1: Double) : this(Point2D(x0, z0), Point2D(x1, z1))

    val dx = max.x - min.x
    val dz = max.z - min.z

    init {
        if (dx < 0 || dz < 0) {
            throw IllegalArgumentException("min > max!!")
        }
    }
    
    fun contains(point: Point2<*>): Boolean {
        val point2D = point.toPoint2D()
        return point2D.x in min.x..max.x && point2D.z in min.z..max.z
    }

    fun intersects(other: AABB): Boolean {
        if (other.max.x <= this.min.x || other.min.x >= this.max.x) {
            return false
        }
        if (other.max.z <= this.min.z || other.min.z >= this.max.z) {
            return false
        }
        return true
    }

    fun offset(offset: Point2D) = offset(offset.x, offset.z)
    fun offset(x: Double, z: Double) = AABB(min.x + x, min.z + z, max.x + x, max.z + z)

}