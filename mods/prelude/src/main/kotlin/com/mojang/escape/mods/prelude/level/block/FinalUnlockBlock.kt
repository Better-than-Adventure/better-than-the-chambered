package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.ITriggerEmitterBlock
import com.mojang.escape.level.block.IUsableBlock
import com.mojang.escape.level.block.WallBlock
import com.mojang.escape.level.physics.Point2I

class FinalUnlockBlock(
    pos: Point2I,
    art: Bitmap,
    tex: Int,
    col: Int,
    override val triggerEmitId: Int
): WallBlock(
    pos = pos,
    art = art,
    tex = tex,
    col = col
), ITriggerEmitterBlock, IUsableBlock {
    private var pressed = false

    override fun onUsed(level: Level, source: Entity, item: Item) {
        if (pressed) return
        if (level.session.player.keys < 4) return
        
        Sound.click1.play()
        pressed = true
        level.session.player.keys = 0
        level.trigger(level.session.player, triggerEmitId, 1)
    }
}