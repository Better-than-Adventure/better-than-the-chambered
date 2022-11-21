package com.mojang.escape.mods.wolf3d.level.block

import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block

class SlidingBlock(wallTex: Int, floorTex: Int) : TexturedSolidBlock(wallTex, floorTex) {
    private var xSlide = 0.0
    private var ySlide = 0.0
    private var dXSlide = 0
    private var dYSlide = 0

    override fun use(level: Level, item: Item): Boolean {
        if (dXSlide == 0 && dYSlide == 0) {
            val player = level.player
            solidRender = false

            if (player.x > x) {
                dXSlide = -1
                return true
            } else if (player.x < x) {
                dXSlide = 1
                return true
            }
            if (player.z > y) {
                dYSlide = -1
                return true
            } else if (player.z < y) {
                dYSlide = 1
                return true
            }
        }
        return false
    }

    override fun tick() {
        if (xSlide >= 1.0 || ySlide >= 1.0 || xSlide <= -1.0 || ySlide <= -1.0) {
            val newBlock = TexturedBlock(this.tex, 8 * 0 + 0)
            newBlock.ceilTex = 8 * 0 + 0
            newBlock.col = this.col
            level!!.setBlock(x, y, newBlock)
            val adjBlock = level!!.getBlock(x + dXSlide + dXSlide, y + dYSlide + dYSlide)
            if (!adjBlock.blocksMotion) {
                xSlide = 0.0
                ySlide = 0.0
                level!!.setBlock(x + dXSlide, y + dYSlide, this)
            } else {
                val newNewBlock = TexturedSolidBlock(this.tex, this.floorTex)
                newNewBlock.col = this.col
                level!!.setBlock(x + dXSlide, y + dYSlide, newNewBlock)
                dXSlide = 0
                dYSlide = 0
            }
        }
        if (dXSlide != 0 || dYSlide != 0) {
            xSlide += dXSlide * 0.025
            ySlide += dYSlide * 0.025
        }
    }

    override fun doRender(bitmap: Bitmap3D, x: Int, z: Int, center: Block, east: Block, south: Block): Boolean {
        if (center is SlidingBlock) {
            if (xSlide == 0.0 && ySlide == 0.0) {
                return false
            } else {
                bitmap.renderWall(x + 0.0 + xSlide, y + 0.0 + ySlide, x + 0.0 + xSlide, y + 1.0 + ySlide, tex, col, wallArt)
                bitmap.renderWall(x + 0.0 + xSlide, y + 0.0 + ySlide, x + 1.0 + xSlide, y + 0.0 + ySlide, tex, col, wallArt)
                bitmap.renderWall(x + 1.0 + xSlide, y + 1.0 + ySlide, x + 0.0 + xSlide, y + 1.0 + ySlide, tex, col, wallArt)
                bitmap.renderWall(x + 1.0 + xSlide, y + 1.0 + ySlide, x + 1.0 + xSlide, y + 0.0 + ySlide, tex, col, wallArt)
                return true
            }
        }
        return false
    }
}