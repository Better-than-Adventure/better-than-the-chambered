package com.mojang.escape

val String.displayWidth: Int
get() {
    return this.length * 6
}

val Int.col: Int
get() {
    return Art.getCol(this)
}