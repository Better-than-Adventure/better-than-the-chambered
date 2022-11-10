package com.mojang.escape.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.RubbleSprite
import com.mojang.escape.level.Level

class VanishBlock: SolidBlock() {
    private var gone = false

    init {
        tex = 1
    }

    override fun use(level: Level, item: Item): Boolean {
        if (gone) {
            return false
        }

        gone = true
        blocksMotion = false
        solidRender = false
        Sound.crumble.play()

        for (i in 0 until 31) {
            val sprite = RubbleSprite()
            sprite.col = this.col
            addSprite(sprite)
        }

        return true
    }
}