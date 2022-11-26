package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.menu.settings.GameSettings
import com.mojang.escape.menu.settings.Settings

class SettingsMenu(lastMenu: Menu? = null): ScrollableMenu(lastMenu) {
    override val title: StringUnit = "gui.menu.settings.title".toTranslatable()
    override val lastButton: StringUnit = "gui.menu.settings.buttonBack".toTranslatable()
    override val numLines: Int = GameSettings.settings.size

    override fun drawLine(target: Bitmap, index: Int, selected: Boolean) {
        val setting = GameSettings.settings[index]
        val str = ((if (selected) "-> " else "") + setting.valueString).toLiteral()
        val col = (if (selected) 0xFFFF80 else 0xA0A0A0).col
        target.draw(setting.name, 0, 0, col)
        target.draw(str, target.width - (6 * str.length), 0, col)
    }

    override fun onLineActivated(game: Game) {
        GameSettings.settings[selected].onActivated(game, GameSettings.settings)
    }

    override fun onLastButtonActivated(game: Game) {
        // Do nothing
    }

    override fun onCancel(game: Game) {
        // Do nothing
    }

    override fun onTick(game: Game) {
        // Do nothing
    }
}