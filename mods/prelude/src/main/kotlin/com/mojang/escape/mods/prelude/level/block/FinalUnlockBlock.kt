package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Item
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.SolidBlock

class FinalUnlockBlock: SolidBlock() {
    private var pressed = false

    init {
        tex = 8 + 3
    }

    override fun use(level: Level, item: Item): Boolean {
        if (pressed) {
            return false
        }
        if (level.player.keys < 4) {
            return false
        }

        Sound.click1.play()
        pressed = true
        level.trigger(id, true)

        return true
    }
}