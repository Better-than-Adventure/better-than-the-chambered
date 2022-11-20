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
    
    private var pickingKey: Settings.KeySetting? = null

    override fun drawLine(target: Bitmap, lineIndex: Int, selected: Boolean) {
        val setting = GameSettings.settings[lineIndex]
        val str = ((if (selected) "-> " else "") + setting.valueString).toLiteral()
        val col = (if (selected) 0xFFFF80 else 0xA0A0A0).col
        target.draw(setting.name, 0, 0, col)
        target.draw(str, target.width - (6 * str.length), 0, col)
    }

    override fun lineActivated(lineIndex: Int) {
        if (GameSettings.settings[lineIndex] is Settings.KeySetting) {
            (GameSettings.settings[lineIndex] as Settings.KeySetting).picking = true
            pickingKey = GameSettings.settings[lineIndex] as Settings.KeySetting
        } else {
            GameSettings.settings[lineIndex].onActivated()
        }
    }

    override fun tick(
        game: Game,
        keys: BooleanArray,
        up: Boolean,
        down: Boolean,
        left: Boolean,
        right: Boolean,
        use: Boolean
    ) {
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
        super.tick(game, keys, up, down, left, right, use)
    }
}