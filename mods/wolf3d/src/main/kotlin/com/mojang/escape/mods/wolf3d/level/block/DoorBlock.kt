package com.mojang.escape.mods.wolf3d.level.block

import com.mojang.escape.Sound
import com.mojang.escape.col
import com.mojang.escape.entities.*
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.SolidBlock
import com.mojang.escape.mods.wolf3d.ModArt
import com.mojang.escape.mods.wolf3d.entities.GuardEntity
import com.mojang.escape.mods.wolf3d.entities.SSEntity

open class DoorBlock: SolidBlock(ModArt.walls, ModArt.floors) {
    var open = false
    var openness = 0.0

    init {
        tex = 8 * 1 + 0
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
            openness += 0.1
        } else {
            openness -= 0.1
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
        if (openness >= openLimit && entity is GuardEntity) {
            return blocksMotion
        }
        if (openness >= openLimit && entity is SSEntity) {
            return blocksMotion
        }
        return true
    }

    override fun doRender(bitmap: Bitmap3D, x: Int, z: Int, center: Block, east: Block, south: Block): Boolean {
        if (center is DoorBlock) {
            val openness = 1 - center.openness * 7 / 8
            if (east.solidRender) {
                bitmap.renderWall(x + openness, z + 0.5, x.toDouble(), z + 0.5, center.tex, 0x00FFFF.col, center.wallArt, 0.0, openness)
                bitmap.renderWall(x.toDouble(), z + 0.5, x + openness, z + 0.5, center.tex, 0x00FFFF.col, center.wallArt, openness, 0.0)
            } else {
                bitmap.renderWall(x + 0.5, z.toDouble(), x + 0.5, z + openness, center.tex,  0x00FFFF.col, center.wallArt, openness, 0.0)
                bitmap.renderWall(x + 0.5, z + openness, x + 0.5, z.toDouble(), center.tex,  0x00FFFF.col, center.wallArt, 0.0, openness)
            }
        }
        return false
    }
}