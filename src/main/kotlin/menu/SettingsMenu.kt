package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.menu.settings.Settings

class SettingsMenu(lastMenu: Menu? = null) : Menu(lastMenu) {
    companion object {
        const val LINES_ON_SCREEN = 6
        var volume = 2
        var keyForward = Keys.KEY_W
        var keyBackward = Keys.KEY_S
        var keyTurnLeft = Keys.KEY_Q
        var keyTurnRight = Keys.KEY_E
        var keyStrafeLeft = Keys.KEY_A
        var keyStrafeRight = Keys.KEY_D
        var keyInteract = Keys.KEY_SPACE
    }

    private var selected = -1
    private var scroll = 0
    private var pickingKey: Settings.KeySetting? = null

    private val settings = Settings {
        rangeSetting("Volume", volume, 0, 4)
            .onChanged { _, newValue ->
                volume = newValue
                Sound.potion.play()
            }
            .valueString {
                arrayOf("0%", "25%", "50%", "75%", "100%")[it]
            }
        keySetting("Forward", keyForward)
            .onChanged { _, newValue ->
                keyForward = newValue
            }
        keySetting("Backward", keyBackward)
            .onChanged { _, newValue ->
                keyBackward = newValue
            }
        keySetting("Turn Left", keyTurnLeft)
            .onChanged { _, newValue ->
                keyTurnLeft = newValue
            }
        keySetting("Turn Right", keyTurnRight)
            .onChanged { _, newValue ->
                keyTurnRight = newValue
            }
        keySetting("Strafe Left", keyStrafeLeft)
            .onChanged { _, newValue ->
                keyStrafeLeft = newValue
            }
        keySetting("Strafe Right", keyStrafeRight)
            .onChanged { _, newValue ->
                keyStrafeRight = newValue
            }
        keySetting("Interact", keyInteract)
            .onChanged { _, newValue ->
                keyInteract = newValue
            }
    }

    override fun render(target: Bitmap) {
        //target.fill(0, 0, 160, 120, 0)

        target.draw("Settings", 40, 8, Art.getCol(0xFFFFFF))

        for (index in scroll until (scroll + LINES_ON_SCREEN)) {
            val setting = settings[index]
            val str = if (index == selected) "-> ${setting.valueString}" else setting.valueString
            target.draw(setting.name, 4, 32 + (index - scroll) * 8, (if (index == selected) 0xFFFF80 else 0xA0A0A0).col)
            target.draw(str, target.width - 4 - str.displayWidth, 32 + (index - scroll) * 8, (if (index == selected) 0xFFFF80 else 0xA0A0A0).col)
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
            if (selected == -1) {
                game.menu = lastMenu ?: TitleMenu()
            } else if (settings[selected] is Settings.KeySetting) {
                (settings[selected] as Settings.KeySetting).picking = true
                pickingKey = settings[selected] as Settings.KeySetting
            } else {
                settings[selected].onActivated()
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