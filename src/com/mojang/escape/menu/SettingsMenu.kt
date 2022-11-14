package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.menu.settings.BaseSetting
import com.mojang.escape.menu.settings.BooleanSetting

class SettingsMenu: Menu() {
    private var selected = -1

    private val settings = arrayOf<BaseSetting<*>>(
        BooleanSetting("Sound", true),
        BooleanSetting("Dummy", false)
    )

    override fun render(target: Bitmap) {
        target.fill(0, 0, 160, 120, 0)

        target.draw("Settings", 40, 8, Art.getCol(0xFFFFFF))

        settings.forEachIndexed { index, setting ->
            val str = if (index == selected) "-> ${setting.valueString}" else setting.valueString
            target.draw(setting.name, 4, 32 + index * 8, (if (index == selected) 0xFFFF80 else 0xA0A0A0).col)
            target.draw(str, target.width - 4 - str.displayWidth, 32 + index * 8, (if (index == selected) 0xFFFF80 else 0xA0A0A0).col)
        }

        val str = if (selected == -1) "-> Back" else "   Back"
        target.draw(str, 40, target.height - 40, (if (selected == -1) 0xFFFF80 else 0xA0A0A0).col)
    }

    override fun tick(game: Game, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (up) {
            if (selected == -1) {
                selected = settings.size - 1
            } else if (selected > 0) {
                selected--
            } else {
                selected = -1
            }
        }
        if (down) {
            if (selected == -1) {
                selected = 0
            } else if (selected < settings.size - 1) {
                selected++
            } else {
                selected = -1
            }
        }
        if (use) {
            Sound.click1.play()
            game.menu = TitleMenu()
        }
    }
}