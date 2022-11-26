package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.menu.WinMenu
import com.mojang.escape.mods.prelude.ModArt

class WinBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): EmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
) {
    override fun onEntityEnter(level: Level, entity: Entity) {
        if (entity is Player) {
            level.session.showMenu(WinMenu(entity))
        }
    }
}