package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.Item
import com.mojang.escape.level.Level

class LockedDoorBlock: DoorBlock() {
    init {
        tex = 5
    }

    override fun use(level: Level, item: Item): Boolean {
        return false
    }

    override fun trigger(pressed: Boolean) {
        open = pressed
    }
}