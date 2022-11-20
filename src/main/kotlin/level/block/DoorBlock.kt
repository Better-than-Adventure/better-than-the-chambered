package com.mojang.escape.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.*
import com.mojang.escape.entities.prelude.OgreEntity
import com.mojang.escape.level.Level

open class DoorBlock: SolidBlock() {
    var open = false
    var openness = 0.0

    init {
        tex = 4
        solidRender = false
    }

    override fun use(level: Level, item: Item): Boolean {
        open = !open
        if (open) {
            Sound.click1.play()
        } else {
            Sound.click2.play()
        }
        return true
    }

    override fun tick() {
        super.tick()

        if (open) {
            openness += 0.2
        } else {
            openness -= 0.2
        }
        if (openness < 0) openness = 0.0
        if (openness > 1) openness = 1.0

        val openLimit = 7 / 8.0
        if (openness < openLimit && !open && !blocksMotion) {
            if (level != null && level!!.containsBlockingEntity(x - 0.5, y - 0.5, x + 0.5, y + 0.5)) {
                openness = openLimit
                return
            }
        }

        blocksMotion = openness < openLimit
    }

    override fun blocks(entity: Entity): Boolean {
        val openLimit = 7 / 8.0
        if (openness >= openLimit && entity is Player) {
            return blocksMotion
        }
        if (openness >= openLimit && entity is Bullet) {
            return blocksMotion
        }
        if (openness >= openLimit && entity is OgreEntity) {
            return blocksMotion
        }
        return true
    }
}