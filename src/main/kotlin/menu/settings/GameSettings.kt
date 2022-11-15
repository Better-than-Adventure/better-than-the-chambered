package com.mojang.escape.menu.settings

import com.mojang.escape.Keys
import com.mojang.escape.Sound

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
            volume = rangeSetting("Volume", 2, 0, 4)
                .onChanged { _, _ ->
                    Sound.potion.play()
                }
                .valueString {
                    arrayOf("0%", "25%", "50%", "75%", "100%")[it]
                }
            as Settings.RangeSetting
            mouseLook = booleanSetting("Mouselook", true)
            graphics = rangeSetting("Graphics", 0, 0, 2)
                .valueString {
                    arrayOf("Full", "EGA", "CGA")[it]
                }
            as Settings.RangeSetting
            keyForward = keySetting("Forward", Keys.KEY_W)
            keyBackward = keySetting("Backward", Keys.KEY_S)
            keyTurnLeft = keySetting("Turn Left", Keys.KEY_Q)
            keyTurnRight = keySetting("Turn Right", Keys.KEY_E)
            keyStrafeLeft = keySetting("Strafe Left", Keys.KEY_A)
            keyStrafeRight = keySetting("Strafe Right", Keys.KEY_D)
            keyInteract = keySetting("Interact", Keys.KEY_SPACE)
            keySlots = arrayOf(
                keySetting("Slot 1", Keys.KEY_1),
                keySetting("Slot 2", Keys.KEY_2),
                keySetting("Slot 3", Keys.KEY_3),
                keySetting("Slot 4", Keys.KEY_4),
                keySetting("Slot 5", Keys.KEY_5),
                keySetting("Slot 6", Keys.KEY_6),
                keySetting("Slot 7", Keys.KEY_7),
                keySetting("Slot 8", Keys.KEY_8)
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