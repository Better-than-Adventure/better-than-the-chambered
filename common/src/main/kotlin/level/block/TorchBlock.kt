package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.Level
import java.util.*

class TorchBlock: Block() {
    private var torchSprite = BasicSprite(0.0, 0.0, 0.0, 3, Art.getCol(0xFFFF00))

    init {
        addSprite(torchSprite)
    }

    override fun decorate(level: Level, x: Int, y: Int) {
        val random = Random(((x + y * 1000) * 341871231).toLong())
        val r = 0.4
        for (i in 0 until 1000) {
            val face = random.nextInt(4)
            if (face == 0 && level.getBlock(x - 1, y).solidRender) {
                torchSprite.x -= r
                break
            }
            if (face == 1 && level.getBlock(x, y - 1).solidRender) {
                torchSprite.z -= r
                break
            }
            if (face == 2 && level.getBlock(x + 1, y).solidRender) {
                torchSprite.x += r
                break
            }
            if (face == 3 && level.getBlock(x, y + 1).solidRender) {
                torchSprite.z += r
                break
            }
        }
    }

    override fun tick() {
        super.tick()
        if (random.nextInt(4) == 0) {
            torchSprite.tex = 3 + random.nextInt(2)
        }
    }
}