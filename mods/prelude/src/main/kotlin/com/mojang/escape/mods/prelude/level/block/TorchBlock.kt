package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.SolidBlock
import com.mojang.escape.mods.prelude.ModArt
import java.util.*

class TorchBlock: Block(ModArt.walls, ModArt.floors) {
    private var torchSprite = BasicSprite(0.0, 0.0, 0.0, 3, Art.getCol(0xFFFF00), ModArt.sprites)

    init {
        addSprite(torchSprite)
    }

    override fun decorate(blocks: Array<Block>, blocksWidth: Int, blocksHeight: Int, x: Int, y: Int) {
        fun getBlock(x: Int, y: Int): Block {
            return blocks.getOrElse(x + y * blocksWidth) {
                level?.solidWall ?: SolidBlock(ModArt.walls, ModArt.floors)
            }
        }
        
        val random = Random(((x + y * 1000) * 341871231).toLong())
        val r = 0.4
        for (i in 0 until 1000) {
            val face = random.nextInt(4)
            if (face == 0 && getBlock(x - 1, y).solidRender) {
                torchSprite.x -= r
                break
            }
            if (face == 1 && getBlock(x, y - 1).solidRender) {
                torchSprite.z -= r
                break
            }
            if (face == 2 && getBlock(x + 1, y).solidRender) {
                torchSprite.x += r
                break
            }
            if (face == 3 && getBlock(x, y + 1).solidRender) {
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