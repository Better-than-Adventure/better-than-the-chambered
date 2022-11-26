package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.mods.ModLoader

class GameTypePickerMenu(lastMenu: Menu? = null): ScrollableMenu(lastMenu) {
    private val mods = ModLoader().getModList()
    
    override val title: StringUnit = "gui.menu.gametype.title".toTranslatable()
    override val lastButton: StringUnit = "gui.menu.gametype.buttonCancel".toTranslatable()
    override val numLines: Int = mods.size
    
    override fun drawLine(target: Bitmap, index: Int, selected: Boolean) {
        val mod = mods[index]
        val str = (if (selected) "-> " else "").toLiteral() + mod.name
        val col = (if (selected) 0xFFFF80 else 0xA0A0A0).col
        target.draw(str, 0, 0, col)
    }

    override fun onLineActivated(game: Game) {
        val mod = mods[selected]

        game.session = GameSession(game, mod)
        game.menu = null
    }

    override fun onLastButtonActivated(game: Game) {
        onCancel(game)
    }

    override fun onCancel(game: Game) {
        game.menu = lastMenu ?: TitleMenu()
    }

    override fun onTick(game: Game) {
        // Do nothing
    }
}