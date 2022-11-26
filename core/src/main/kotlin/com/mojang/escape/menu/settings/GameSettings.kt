package com.mojang.escape.menu.settings

import com.mojang.escape.Sound
import com.mojang.escape.lang.Language
import com.mojang.escape.toTranslatable

class GameSettings {
    companion object {
        lateinit var language: Settings.LanguageSetting
        lateinit var volume: Settings.RangeSetting
        lateinit var mouseLook: Settings.BooleanSetting
        lateinit var graphics: Settings.RangeSetting

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