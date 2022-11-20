package com.mojang.escape

import com.mojang.escape.entities.Item
import com.mojang.escape.entities.OgreEntity
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import com.mojang.escape.level.PNGLevel
import com.mojang.escape.level.Wolf3DLevel

enum class GameType(val displayName: StringUnit) {
    PRELUDE("gametype.prelude".toTranslatable()),
    WOLF3D("gametype.wolf3d".toTranslatable());
    
    fun getFirstLevel(game: Game): Level {
        return when (this) {
            PRELUDE -> PNGLevel.loadPNGLevel(game, "start")
            WOLF3D -> Wolf3DLevel.loadWolf3DLevel(game, Wolf3DLevel.gameMaps.levelHeaders.first())
        }
    }
    
    fun onNewGame(game: Game) {
        if (this == WOLF3D) {
            game.player!!.items[0] = Item.PowerGlove
            game.player!!.items[1] = Item.Pistol
            game.player!!.ammo = 20
            OgreEntity.dropAmmo = true
        } else {
            OgreEntity.dropAmmo = false
        }
    }
}