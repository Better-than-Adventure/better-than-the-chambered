package com.mojang.escape.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Item
import com.mojang.escape.level.Level

class SwitchBlock: SolidBlock() {
    private var pressed = false

    init {
        tex = 2
    }

    override fun use(level: Level, item: Item): Boolean {
        pressed = !pressed
        tex = when (pressed) {
            true -> 3
            false -> 2
        }

        level.trigger(id, pressed)
        when (pressed) {
            true -> Sound.click1
            false -> Sound.click2
        }.play()

        return true
    }
}