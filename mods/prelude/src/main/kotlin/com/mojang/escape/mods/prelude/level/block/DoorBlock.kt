package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.*
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.SolidBlock
import com.mojang.escape.mods.prelude.entities.OgreEntity

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

    override fun doRender(bitmap: Bitmap3D, x: Int, z: Int, center: Block, east: Block, south: Block): Boolean {
        if (center is DoorBlock) {
            val rr = 1 / 8.0
            val openness = 1 - center.openness * 7 / 8
            if (east.solidRender) {
                bitmap.renderWall(x + openness, z + 0.5 - rr, x.toDouble(), z + 0.5 - rr, center.tex, (center.col and 0xFEFEFE) shr 1, 0.0, openness)
                bitmap.renderWall(x.toDouble(), z + 0.5 + rr, x + openness, z + 0.5 + rr, center.tex, (center.col and 0xFEFEFE) shr 1, openness, 0.0)
                bitmap.renderWall(x + openness, z + 0.5 + rr, x + openness, z + 0.5 - rr, center.tex,  center.col, 0.5 - rr, 0.5 + rr)
            } else {
                bitmap.renderWall(x + 0.5 - rr, z.toDouble(), x + 0.5 - rr, z + openness, center.tex,  center.col, openness, 0.0)
                bitmap.renderWall(x + 0.5 + rr, z + openness, x + 0.5 + rr, z.toDouble(), center.tex,  center.col, 0.0, openness)
                bitmap.renderWall(x + 0.5 - rr, z + openness, x + 0.5 + rr, z + openness, center.tex, (center.col and 0xFEFEFE) shr 1, 0.5 - rr, 0.5 + rr)
            }
        }
        return false
    }
}