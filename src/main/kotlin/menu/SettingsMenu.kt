package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.menu.settings.GameSettings
import com.mojang.escape.menu.settings.Settings

class SettingsMenu(lastMenu: Menu? = null) : Menu(lastMenu) {
    companion object {
        const val LINES_ON_SCREEN = 6
    }

    private var selected = 0
    private var scroll = 0
    private var pickingKey: Settings.KeySetting? = null

    override fun render(target: Bitmap) {
        target.draw("Settings", 40, 8, Art.getCol(0xFFFFFF))

        val scrollProgress = scroll.toDouble() / (GameSettings.settings.size - LINES_ON_SCREEN).toDouble()
        val scrollbarY = scrollProgress * (LINES_ON_SCREEN - 1) * 8
        target.fill(target.width - 6, 32, target.width - 1, 32 + LINES_ON_SCREEN * 8 - 1, 0x505050.col)
        target.draw("ç", target.width - 6, 32 + scrollbarY.toInt(), 0xA0A0A0.col)

        for (index in scroll until (scroll + LINES_ON_SCREEN)) {
            val setting = GameSettings.settings[index]
            val str = if (index == selected) "-> ${setting.valueString}" else setting.valueString
            target.draw(setting.name, 4, 32 + (index - scroll) * 8, (if (index == selected) 0xFFFF80 else 0xA0A0A0).col)
            target.draw(str, target.width - 8 - str.displayWidth, 32 + (index - scroll) * 8, (if (index == selected) 0xFFFF80 else 0xA0A0A0).col)
        }

        val str = if (selected == -1) "-> Back" else "   Back"
        target.draw(str, 40, target.height - 32, (if (selected == -1) 0xFFFF80 else 0xA0A0A0).col)
    }

    override fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (pickingKey != null) {
            val pickingKey = pickingKey!!
            if (keys[Keys.KEY_ESCAPE.ordinal]) {
                pickingKey.picking = false
                this.pickingKey = null
            } else {
                val keyIndex = keys.indexOfFirst {
                    it
                }
                if (keyIndex != -1) {
                    pickingKey.value = Keys.values()[keyIndex]
                    pickingKey.picking = false
                    this.pickingKey = null
                }
            }
            return
        }
        if (up) {
            if (selected == -1) {
                selected = GameSettings.settings.size - 1
            } else if (selected > 0) {
                selected--
            } else {
                selected = -1
            }
        }
        if (down) {
            if (selected == -1) {
                selected = 0
            } else if (selected < GameSettings.settings.size - 1) {
                selected++
            } else {
                selected = -1
            }
        }
        if (left || right) {
            selected = -1
        }
        if (use) {
            Sound.click1.play()
            if (selected == -1) {
                game.menu = lastMenu ?: TitleMenu()
            } else if (GameSettings.settings[selected] is Settings.KeySetting) {
                (GameSettings.settings[selected] as Settings.KeySetting).picking = true
                pickingKey = GameSettings.settings[selected] as Settings.KeySetting
            } else {
                GameSettings.settings[selected].onActivated()
            }
        }

        if (selected != -1) {
            if (scroll > selected) {
                scroll = selected
            }
            if (scroll + LINES_ON_SCREEN <= selected) {
                scroll = selected - LINES_ON_SCREEN + 1
            }
        }
    }
}