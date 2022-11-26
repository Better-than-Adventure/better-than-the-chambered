package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB

class KeyEntity(
    pos: Point3D,
    rot: Double,
    vel: Point3D,
    rotVel: Double
): ItemEntity(
    pos = pos,
    rot = rot,
    vel = vel,
    rotVel = rotVel,
    collisionBox = RelativeAABB(0.01),
    art = Art.sprites,
    tex = 8 * 2 + 0,
    col = 0x00FFFF
) {
    override fun onPickup(level: Level, player: Player) {
        player.keys++
    }
}