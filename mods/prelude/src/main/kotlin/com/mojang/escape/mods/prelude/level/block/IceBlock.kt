package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.*
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModSound
import com.mojang.escape.mods.prelude.entities.EyeBossEntity
import com.mojang.escape.mods.prelude.entities.EyeEntity
import kotlin.math.cos
import kotlin.math.sin

class IceBlock: Block() {
    companion object {
        var playerSliding = false
    }
    
    init {
        blocksMotion = false
        floorTex = 16
    }

    override fun tick() {
        super.tick()
        floorCol = Art.getCol(0x8080FF)
    }

    override fun getWalkSpeed(player: Player): Double {
        if (player.selectedItem == Item.Skates) return 0.05
        return 1.4
    }

    override fun getFriction(player: Player): Double {
        if (player.selectedItem == Item.Skates) return 0.98
        return 1.0
    }

    override fun onPlayerEnter(player: Player) {
        ModSound.slide.play()
    }

    override fun onPlayerWalk(player: Player, xm: Double, zm: Double): Boolean {
        if (player.selectedItem != Item.Skates) {
            if (player.xa * player.xa > player.za * player.za) {
                player.za = 0.0
                player.xa = if (player.xa > 0.0) {
                    0.08
                } else {
                    -0.08
                }
                player.z += ((player.z + 0.5).toInt() - player.z) * 0.2
            } else if (player.xa * player.xa < player.za * player.za) {
                player.xa = 0.0
                player.za = if (player.za > 0) {
                    0.08
                } else {
                    -0.08
                }
                player.x += ((x + 0.5).toInt() - x) * 0.2
            } else {
                player.xa -= (xm * cos(player.rot) + zm * sin(player.rot)) * 0.1
                player.za -= (zm * cos(player.rot) - xm * sin(player.rot)) * 0.1
            }
            return true
        }

        return false
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is Player) {
            return false
        }
        if (entity is Bullet) {
            return false
        }
        if (entity is EyeBossEntity) {
            return false
        }
        if (entity is EyeEntity) {
            return false
        }
        return true
    }
}