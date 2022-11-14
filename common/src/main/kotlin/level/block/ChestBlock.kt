package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.Level

class ChestBlock: Block() {
    private val chestSprite = BasicSprite(0.0, 0.0, 0.0, 8 * 2 + 0, Art.getCol(0xffff00))
    private var open = false

    init {
        tex = 1
        blocksMotion = true

        addSprite(chestSprite)
    }

    override fun use(level: Level, item: Item): Boolean {
        if (open) {
            return false
        }

        chestSprite.tex = chestSprite.tex + 1
        open = true

        level.getLoot(id)
        Sound.treasure.play()

        return true
    }
}