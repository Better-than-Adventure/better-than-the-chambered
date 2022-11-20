package com.mojang.escape.menu.settings

import com.mojang.escape.Game
import com.mojang.escape.Keys
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

    abstract class Setting<T>(private val settings: Settings, val key: String, val name: StringUnitTranslatable, value: T) {
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
                Settings.writeToFile(settings)
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

        abstract fun onActivated()

        abstract fun toConfigString(): String
        abstract fun fromConfigString(string: String)
    }

    class RangeSetting(settings: Settings, key: String, name: StringUnitTranslatable, value: Int, private val minValue: Int, private val maxValue: Int): Setting<Int>(settings, key, name, value) {
        override var value: Int
            get() = super.value
            set(value) {
                var realValue = value
                if (value < 0) realValue = maxValue
                if (value > maxValue) realValue = minValue
                super.value = realValue
            }

        override fun onActivated() {
            value++
        }

        override fun toConfigString(): String {
            return value.toString()
        }

        override fun fromConfigString(string: String) {
            value = string.toInt()
        }
    }

    class BooleanSetting(settings: Settings, key: String, name: StringUnitTranslatable, value: Boolean): Setting<Boolean>(settings, key, name, value) {
        override fun onActivated() {
            value = !value
        }

        override fun toConfigString(): String {
            return value.toString()
        }

        override fun fromConfigString(string: String) {
            value = string.toBoolean()
        }
    }

    class KeySetting(settings: Settings, key: String, name: StringUnitTranslatable, value: Keys): Setting<Keys>(settings, key, name, value) {
        var picking = false

        override val valueString: String
            get() = if (picking) "?" else value.displayName

        override fun onActivated() {
            // Do nothing
        }

        override fun toConfigString(): String {
            return value.ordinal.toString()
        }

        override fun fromConfigString(string: String) {
            value = Keys.values()[string.toInt()]
        }
    }
    
    class LanguageSetting(settings: Settings, key: String, name: StringUnitTranslatable, value: Language): Setting<Language>(settings, key, name, value) {
        override fun onActivated() {
            Game.theGame?.menu = LanguagesMenu(Game.theGame?.menu) 
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
        val setting = RangeSetting(this, key, name, value, minValue, maxValue)
        add(setting)
        return setting
    }

    fun booleanSetting(key: String, name: StringUnitTranslatable, value: Boolean): BooleanSetting {
        val setting = BooleanSetting(this, key, name, value)
        add(setting)
        return setting
    }

    fun keySetting(key: String, name: StringUnitTranslatable, value: Keys): KeySetting {
        val setting = KeySetting(this, key, name, value)
        add(setting)
        return setting
    }
    
    fun languagesSetting(key: String, name: StringUnitTranslatable, value: Language): LanguageSetting {
        val setting = LanguageSetting(this, key, name, value)
        add(setting)
        return setting
    }
}