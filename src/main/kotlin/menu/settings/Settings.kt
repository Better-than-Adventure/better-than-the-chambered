package com.mojang.escape.menu.settings

import com.mojang.escape.Keys
import com.mojang.escape.translated
import com.mojang.escape.util.TranslatedString
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.EnumMap
import java.util.Properties

class Settings(init: Settings.() -> Unit): MutableList<Settings.Setting<*>> by mutableListOf() {
    companion object {
        fun writeToFile(settings: Settings) {
            FileOutputStream("config.properties").use {
                val prop = Properties()
                var comments = ""
                for (setting in settings) {
                    prop.setProperty("config.${setting.name.key.lowercase()}", setting.toConfigString().lowercase())
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
                        setting.fromConfigString(prop.getProperty("config.${setting.name.key.lowercase()}"))
                    }
                }
            }
        }
    }

    abstract class Setting<T>(private val settings: Settings, val name: TranslatedString, value: T) {
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

    class RangeSetting(settings: Settings, name: TranslatedString, value: Int, private val minValue: Int, private val maxValue: Int): Setting<Int>(settings, name, value) {
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

    class BooleanSetting(settings: Settings, name: TranslatedString, value: Boolean): Setting<Boolean>(settings, name, value) {
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

    class KeySetting(settings: Settings, name: TranslatedString, value: Keys): Setting<Keys>(settings, name, value) {
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

    init {
        init(this)
    }

    fun rangeSetting(name: TranslatedString, value: Int, minValue: Int, maxValue: Int): RangeSetting {
        val setting = RangeSetting(this, name, value, minValue, maxValue)
        add(setting)
        return setting
    }

    fun booleanSetting(name: TranslatedString, value: Boolean): BooleanSetting {
        val setting = BooleanSetting(this, name, value)
        add(setting)
        return setting
    }

    fun keySetting(name: TranslatedString, value: Keys): KeySetting {
        val setting = KeySetting(this, name, value)
        add(setting)
        return setting
    }
}