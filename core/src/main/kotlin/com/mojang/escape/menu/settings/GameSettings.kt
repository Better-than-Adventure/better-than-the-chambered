package com.mojang.escape.menu.settings

import com.mojang.escape.Keys
import com.mojang.escape.Sound
import com.mojang.escape.lang.Language
import com.mojang.escape.toTranslatable

class GameSettings {
    companion object {
        lateinit var language: Settings.LanguageSetting
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
            language = languagesSetting("language", "settings.language.name".toTranslatable(), Language("en_US"))
            volume = rangeSetting("volume", "settings.volume.name".toTranslatable(), 2, 0, 4)
                .onChanged { _, _ ->
                    Sound.potion.play()
                }
                .valueString {
                    arrayOf(
                        "settings.volume.valueString.0".toTranslatable(),
                        "settings.volume.valueString.25".toTranslatable(),
                        "settings.volume.valueString.50".toTranslatable(),
                        "settings.volume.valueString.75".toTranslatable(),
                        "settings.volume.valueString.100".toTranslatable()
                    )[it].value
                }
            as Settings.RangeSetting
            mouseLook = booleanSetting("mouselook", "settings.mouselook.name".toTranslatable(), true)
                .valueString {
                    if (it) {
                        "settings.mouselook.valueString.on".toTranslatable()
                    } else {
                        "settings.mouselook.valueString.off".toTranslatable()
                    }.value
                }
            as Settings.BooleanSetting
            graphics = rangeSetting("graphics", "settings.graphics.name".toTranslatable(), 0, 0, 2)
                .valueString {
                    arrayOf(
                        "settings.graphics.valueString.full".toTranslatable(),
                        "settings.graphics.valueString.ega".toTranslatable(),
                        "settings.graphics.valueString.cga".toTranslatable()
                    )[it].value
                }
            as Settings.RangeSetting
            keyForward = keySetting("keyForward", "settings.keyForward.name".toTranslatable(), Keys.KEY_W)
            keyBackward = keySetting("keyBackward", "settings.keyBackward.name".toTranslatable(), Keys.KEY_S)
            keyTurnLeft = keySetting("keyTurnLeft", "settings.keyTurnLeft.name".toTranslatable(), Keys.KEY_Q)
            keyTurnRight = keySetting("keyTurnRight", "settings.keyTurnRight.name".toTranslatable(), Keys.KEY_E)
            keyStrafeLeft = keySetting("keyStrafeLeft", "settings.keyStrafeLeft.name".toTranslatable(), Keys.KEY_A)
            keyStrafeRight = keySetting("keyStrafeRight", "settings.keyStrafeRight.name".toTranslatable(), Keys.KEY_D)
            keyInteract = keySetting("keyInteract", "settings.keyInteract.name".toTranslatable(), Keys.KEY_SPACE)
            keySlots = arrayOf(
                keySetting("keySlot0", "settings.keySlots.1.name".toTranslatable(), Keys.KEY_1),
                keySetting("keySlot1", "settings.keySlots.2.name".toTranslatable(), Keys.KEY_2),
                keySetting("keySlot2", "settings.keySlots.3.name".toTranslatable(), Keys.KEY_3),
                keySetting("keySlot3", "settings.keySlots.4.name".toTranslatable(), Keys.KEY_4),
                keySetting("keySlot4", "settings.keySlots.5.name".toTranslatable(), Keys.KEY_5),
                keySetting("keySlot5", "settings.keySlots.6.name".toTranslatable(), Keys.KEY_6),
                keySetting("keySlot6", "settings.keySlots.7.name".toTranslatable(), Keys.KEY_7),
                keySetting("keySlot7", "settings.keySlots.8.name".toTranslatable(), Keys.KEY_8)
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