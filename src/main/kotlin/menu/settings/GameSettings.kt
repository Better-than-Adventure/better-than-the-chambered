package com.mojang.escape.menu.settings

import com.mojang.escape.Keys
import com.mojang.escape.Sound
import com.mojang.escape.translatable

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
            volume = rangeSetting("volume", "settings.volume.name".translatable, 2, 0, 4)
                .onChanged { _, _ ->
                    Sound.potion.play()
                }
                .valueString {
                    arrayOf(
                        "settings.volume.valueString.0".translatable,
                        "settings.volume.valueString.25".translatable,
                        "settings.volume.valueString.50".translatable,
                        "settings.volume.valueString.75".translatable,
                        "settings.volume.valueString.100".translatable
                    )[it].translated
                }
            as Settings.RangeSetting
            mouseLook = booleanSetting("mouselook", "settings.mouselook.name".translatable, true)
                .valueString {
                    if (it) {
                        "settings.mouselook.valueString.on".translatable
                    } else {
                        "settings.mouselook.valueString.off".translatable
                    }.translated
                }
            as Settings.BooleanSetting
            graphics = rangeSetting("graphics", "settings.graphics.name".translatable, 0, 0, 2)
                .valueString {
                    arrayOf(
                        "settings.graphics.valueString.full".translatable,
                        "settings.graphics.valueString.ega".translatable,
                        "settings.graphics.valueString.cga".translatable
                    )[it].translated
                }
            as Settings.RangeSetting
            keyForward = keySetting("keyForward", "settings.keyForward.name".translatable, Keys.KEY_W)
            keyBackward = keySetting("keyBackward", "settings.keyBackward.name".translatable, Keys.KEY_S)
            keyTurnLeft = keySetting("keyTurnLeft", "settings.keyTurnLeft.name".translatable, Keys.KEY_Q)
            keyTurnRight = keySetting("keyTurnRight", "settings.keyTurnRight.name".translatable, Keys.KEY_E)
            keyStrafeLeft = keySetting("keyStrafeLeft", "settings.keyStrafeLeft.name".translatable, Keys.KEY_A)
            keyStrafeRight = keySetting("keyStrafeRight", "settings.keyStrafeRight.name".translatable, Keys.KEY_D)
            keyInteract = keySetting("keyInteract", "settings.keyInteract.name".translatable, Keys.KEY_SPACE)
            keySlots = arrayOf(
                keySetting("keySlot0", "settings.keySlots.1.name".translatable, Keys.KEY_1),
                keySetting("keySlot1", "settings.keySlots.2.name".translatable, Keys.KEY_2),
                keySetting("keySlot2", "settings.keySlots.3.name".translatable, Keys.KEY_3),
                keySetting("keySlot3", "settings.keySlots.4.name".translatable, Keys.KEY_4),
                keySetting("keySlot4", "settings.keySlots.5.name".translatable, Keys.KEY_5),
                keySetting("keySlot5", "settings.keySlots.6.name".translatable, Keys.KEY_6),
                keySetting("keySlot6", "settings.keySlots.7.name".translatable, Keys.KEY_7),
                keySetting("keySlot7", "settings.keySlots.8.name".translatable, Keys.KEY_8)
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