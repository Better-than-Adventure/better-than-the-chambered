package com.mojang.escape.menu.settings

import com.mojang.escape.Game
import com.mojang.escape.lang.Language
import com.mojang.escape.lang.StringUnitTranslatable
import com.mojang.escape.menu.LanguagesMenu
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

class Settings(init: Settings.() -> Unit): MutableList<Settings.Setting<*>> by mutableListOf() {
    companion object {
        fun writeToFile(settings: Settings) {
            FileOutputStream("config.properties").use {
                val prop = Properties()
                var comments = ""
                for (setting in settings) {
                    prop.setProperty("config.${setting.key.lowercase()}", setting.toConfigString().lowercase())
                    comments += "${setting.name}: ${setting::class.simpleName}\n"
                }

                prop.store(it, comments)
            }
        }

        fun readFromFile(settings: Settings) {
            if (File("config.properties").exists()) {
                FileInputStream("config.properties").use {
                    val prop = Properties()
                    prop.load(it)

                    for (setting in settings) {
                        setting.fromConfigString(prop.getProperty("config.${setting.key.lowercase()}"))
                    }
                }
            }
        }
    }

    abstract class Setting<T>(val key: String, val name: StringUnitTranslatable, value: T) {
        private var _onChanged: ((oldValue: T, newValue: T) -> Unit)? = null
        private var _valueString: ((value: T) -> String)? = null

        private var _value = value
        open var value: T
            get() {
                return _value
            }
            set(value) {
                val oldValue = _value
                _value = value
                _onChanged?.invoke(oldValue, value)
            }

        open val valueString: String
            get() {
                return _valueString?.invoke(value) ?: _value.toString().uppercase()
            }

        fun onChanged(listener: (oldValue: T, newValue: T) -> Unit): Setting<T> {
            _onChanged = listener
            return this
        }
        fun valueString(listener: (value: T) -> String): Setting<T> {
            _valueString = listener
            return this
        }

        abstract fun onActivated(game: Game, settings: Settings)

        abstract fun toConfigString(): String
        abstract fun fromConfigString(string: String)
    }

    class RangeSetting(key: String, name: StringUnitTranslatable, value: Int, private val minValue: Int, private val maxValue: Int): Setting<Int>(key, name, value) {
        override var value: Int
            get() = super.value
            set(value) {
                var realValue = value
                if (value < 0) realValue = maxValue
                if (value > maxValue) realValue = minValue
                super.value = realValue
            }

        override fun onActivated(game: Game, settings: Settings) {
            value++
        }

        override fun toConfigString(): String {
            return value.toString()
        }

        override fun fromConfigString(string: String) {
            value = string.toInt()
        }
    }

    class BooleanSetting(key: String, name: StringUnitTranslatable, value: Boolean): Setting<Boolean>(key, name, value) {
        override fun onActivated(game: Game, settings: Settings) {
            value = !value
        }

        override fun toConfigString(): String {
            return value.toString()
        }

        override fun fromConfigString(string: String) {
            value = string.toBoolean()
        }
    }
    
    class LanguageSetting(key: String, name: StringUnitTranslatable, value: Language): Setting<Language>(key, name, value) {
        override fun onActivated(game: Game, settings: Settings) {
            game.menu = LanguagesMenu(game.menu) 
        }

        override fun toConfigString(): String {
            return value.name
        }

        override fun fromConfigString(string: String) {
            value = Language(string)
        }
    }

    init {
        init(this)
    }

    fun rangeSetting(key: String, name: StringUnitTranslatable, value: Int, minValue: Int, maxValue: Int): RangeSetting {
        val setting = RangeSetting(key, name, value, minValue, maxValue)
        add(setting)
        return setting
    }

    fun booleanSetting(key: String, name: StringUnitTranslatable, value: Boolean): BooleanSetting {
        val setting = BooleanSetting(key, name, value)
        add(setting)
        return setting
    }
    
    fun languagesSetting(key: String, name: StringUnitTranslatable, value: Language): LanguageSetting {
        val setting = LanguageSetting(key, name, value)
        add(setting)
        return setting
    }
}