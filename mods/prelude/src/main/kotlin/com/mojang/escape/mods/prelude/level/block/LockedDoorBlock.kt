package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2I

class LockedDoorBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int,
    doorArt: Bitmap,
    doorTex: Int,
    doorCol: Int,
    triggerId: Int
): DoorBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol,
    doorArt = doorArt,
    doorTex = doorTex,
    doorCol = doorCol,
    triggerId = triggerId
) {
    override fun onUsed(level: Level, source: Entity, item: Item) {
        // Do nothing
    }
}