package com.mojang.escape.level.block

open class SolidBlock: Block() {
    init {
        solidRender = true
        blocksMotion = true
    }
}