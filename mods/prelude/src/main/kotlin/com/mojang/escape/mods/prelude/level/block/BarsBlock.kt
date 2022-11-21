package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class BarsBlock: Block(ModArt.walls, ModArt.floors) {
    private val sprite: Sprite = BasicSprite(0.0, 0.0, 0.0, 0, 0x202020, ModArt.sprites)
    private var open: Boolean = false

    init {
        addSprite(sprite)
        blocksMotion = true
    }

    override fun use(level: Level, item: Item): Boolean {
        if (open) {
            return false
        }

        if (item == Item.Cutters) {
            ModSound.cut.play()
            sprite.tex = 1
            open = true
        }

        return true
    }

    override fun blocks(entity: Entity): Boolean {
        if (open && entity is Player) {
            return false
        }
        if (open && entity is Bullet) {
            return false
        }
        return blocksMotion
    }
}