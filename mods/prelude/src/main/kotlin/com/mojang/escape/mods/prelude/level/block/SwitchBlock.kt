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
import com.mojang.escape.mods.prelude.ModArt

class SwitchBlock(
    pos: Point2I,
    col: Int, 
    override val triggerEmitId: Int
): WallBlock(
    pos = pos,
    art = ModArt.walls,
    tex = 2,
    col = col
), IUsableBlock, ITriggerEmitterBlock {
    private var pressed = false

    override fun onUsed(level: Level, source: Entity, item: Item) {
        pressed = !pressed
        tex = when (pressed) {
            true -> 3
            false -> 2
        }
        level.trigger(source, triggerEmitId, if (pressed) 1 else 0)
        when (pressed) {
            true -> Sound.click1
            false -> Sound.click2
        }.play()
    }
}