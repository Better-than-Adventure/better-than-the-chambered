package com.mojang.escape.menu.settings

import com.mojang.escape.Keys
import com.mojang.escape.Sound
import com.mojang.escape.translated

class GameSettings {
    companion object {
        lateinit var volume: Settings.RangeSetting
        lateinit var mouseLook: Settings.BooleanSetting
        lateinit var graphics: Settings.RangeSetting
        lateinit var keyForward: Settings.KeySetting
        lateinit var keyBackward: Settings.KeySetting
        lateinit var keyTurnLeft: Settings.KeySetting
        lateinit var keyTurnRight: Settings.KeySetting
        lateinit var keyStrafeLeft: Settings.KeySetting
        lateinit var keyStrafeRight: Settings.KeySetting
        lateinit var keyInteract: Settings.KeySetting
        lateinit var keySlots: Array<Settings.KeySetting>

        val settings = Settings {
            volume = rangeSetting("settings.volume.name".translated, 2, 0, 4)
                .onChanged { _, _ ->
                    Sound.potion.play()
                }
                .valueString {
                    arrayOf(
                        "settings.volume.valueString.0".translated,
                        "settings.volume.valueString.25".translated,
                        "settings.volume.valueString.50".translated,
                        "settings.volume.valueString.75".translated,
                        "settings.volume.valueString.100".translated
                    )[it].value
                }
            as Settings.RangeSetting
            mouseLook = booleanSetting("settings.mouselook.name".translated, true)
                .valueString {
                    if (it) {
                        "settings.mouselook.valueString.on".translated
                    } else {
                        "settings.mouselook.valueString.off".translated
                    }.value
                }
            as Settings.BooleanSetting
            graphics = rangeSetting("settings.graphics.name".translated, 0, 0, 2)
                .valueString {
                    arrayOf(
                        "settings.graphics.valueString.full".translated,
                        "settings.graphics.valueString.ega".translated,
                        "settings.graphics.valueString.cga".translated
                    )[it].value
                }
            as Settings.RangeSetting
            keyForward = keySetting("settings.keyForward.name".translated, Keys.KEY_W)
            keyBackward = keySetting("settings.keyBackward.name".translated, Keys.KEY_S)
            keyTurnLeft = keySetting("settings.keyTurnLeft.name".translated, Keys.KEY_Q)
            keyTurnRight = keySetting("settings.keyTurnRight.name".translated, Keys.KEY_E)
            keyStrafeLeft = keySetting("settings.keyStrafeLeft.name".translated, Keys.KEY_A)
            keyStrafeRight = keySetting("settings.keyStrafeRight.name".translated, Keys.KEY_D)
            keyInteract = keySetting("settings.keyInteract.name".translated, Keys.KEY_SPACE)
            keySlots = arrayOf(
                keySetting("settings.keySlots.1.name".translated, Keys.KEY_1),
                keySetting("settings.keySlots.2.name".translated, Keys.KEY_2),
                keySetting("settings.keySlots.3.name".translated, Keys.KEY_3),
                keySetting("settings.keySlots.4.name".translated, Keys.KEY_4),
                keySetting("settings.keySlots.5.name".translated, Keys.KEY_5),
                keySetting("settings.keySlots.6.name".translated, Keys.KEY_6),
                keySetting("settings.keySlots.7.name".translated, Keys.KEY_7),
                keySetting("settings.keySlots.8.name".translated, Keys.KEY_8)
            )
        }

        init {
            try {
                Settings.readFromFile(settings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}