package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Item
import com.mojang.escape.mods.prelude.gui.RubbleSprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.SolidBlock
import com.mojang.escape.mods.prelude.ModSound

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
        ModSound.crumble.play()

        for (i in 0 until 31) {
            val sprite = RubbleSprite()
            sprite.col = this.col
            addSprite(sprite)
        }

        return true
    }
}