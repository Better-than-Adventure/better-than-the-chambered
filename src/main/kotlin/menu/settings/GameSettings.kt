package com.mojang.escape.menu.settings

import com.mojang.escape.Keys
import com.mojang.escape.Sound

class GameSettings {
    companion object {
        var volume = 2
        var graphics = 0
        var keyForward = Keys.KEY_W
        var keyBackward = Keys.KEY_S
        var keyTurnLeft = Keys.KEY_Q
        var keyTurnRight = Keys.KEY_E
        var keyStrafeLeft = Keys.KEY_A
        var keyStrafeRight = Keys.KEY_D
        var keyInteract = Keys.KEY_SPACE
        val keySlots = arrayOf(Keys.KEY_1, Keys.KEY_2, Keys.KEY_3, Keys.KEY_4, Keys.KEY_5, Keys.KEY_6, Keys.KEY_7, Keys.KEY_8)

        val settings = Settings {
            rangeSetting("Volume", volume, 0, 4)
                .onChanged { _, newValue ->
                    volume = newValue
                    Sound.potion.play()
                }
                .valueString {
                    arrayOf("0%", "25%", "50%", "75%", "100%")[it]
                }
            rangeSetting("Graphics", graphics, 0, 2)
                .onChanged { _, newValue ->
                    graphics = newValue
                }
                .valueString {
                    arrayOf("VGA", "EGA", "CGA")[it]
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
            keySetting("Slot 1", keySlots[0])
                .onChanged { _, newValue ->
                    keySlots[0] = newValue
                }
            keySetting("Slot 2", keySlots[1])
                .onChanged { _, newValue ->
                    keySlots[1] = newValue
                }
            keySetting("Slot 3", keySlots[2])
                .onChanged { _, newValue ->
                    keySlots[2] = newValue
                }
            keySetting("Slot 4", keySlots[3])
                .onChanged { _, newValue ->
                    keySlots[3] = newValue
                }
            keySetting("Slot 5", keySlots[4])
                .onChanged { _, newValue ->
                    keySlots[4] = newValue
                }
            keySetting("Slot 6", keySlots[5])
                .onChanged { _, newValue ->
                    keySlots[5] = newValue
                }
            keySetting("Slot 7", keySlots[6])
                .onChanged { _, newValue ->
                    keySlots[6] = newValue
                }
            keySetting("Slot 8", keySlots[7])
                .onChanged { _, newValue ->
                    keySlots[7] = newValue
                }
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