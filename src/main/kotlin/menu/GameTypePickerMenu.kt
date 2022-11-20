package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level

class GameTypePickerMenu(lastMenu: Menu? = null): ScrollableMenu(lastMenu) {
    override val title: StringUnit = "gui.menu.gametype.title".toTranslatable()
    override val lastButton: StringUnit = "gui.menu.gametype.buttonCancel".toTranslatable()
    override val numLines: Int = GameType.values().size
    
    override fun drawLine(target: Bitmap, lineIndex: Int, selected: Boolean) {
        val gametype = GameType.values()[lineIndex]
        val str = (if (selected) "-> " else "").toLiteral() + gametype.displayName
        val col = (if (selected) 0xFFFF80 else 0xA0A0A0).col
        target.draw(str, 0, 0, col)
    }

    override fun lineActivated(lineIndex: Int) {
        val gametype = GameType.values()[lineIndex]
        Game.theGame!!.gameType = gametype

        Level.clear()

        val localLevel = gametype.getFirstLevel(Game.theGame!!)
        val localPlayer = Player()

        localPlayer.level = localLevel
        localLevel.player = localPlayer
        localPlayer.x = localLevel.xSpawn.toDouble()
        localPlayer.z = localLevel.ySpawn.toDouble()
        localLevel.addEntity(localPlayer)
        localPlayer.rot = Math.PI + 0.4

        Game.theGame!!.level = localLevel
        Game.theGame!!.player = localPlayer

        gametype.onNewGame(Game.theGame!!)
        Game.theGame!!.menu = null
    }
}