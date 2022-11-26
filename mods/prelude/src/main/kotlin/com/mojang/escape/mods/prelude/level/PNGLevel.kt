package com.mojang.escape.mods.prelude.level

import com.mojang.escape.GameSession
import com.mojang.escape.entities.Entity
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.physics.Point2I

class PNGLevel(
    session: GameSession,
    name: StringUnit,
    lengthX: Int,
    lengthZ: Int,
    spawn: Point2I,
    blocks: Array<Block>,
    entities: MutableList<Entity>
): Level(
    session,
    name,
    lengthX,
    lengthZ,
    spawn,
    blocks,
    entities
)