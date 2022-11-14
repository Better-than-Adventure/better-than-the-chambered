package com.mojang.escape.menu.settings

class Settings(init: Settings.() -> Unit): MutableList<Settings.Setting<*>> by mutableListOf() {
    abstract class Setting<T>(val name: String, value: T) {
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

        val valueString: String
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
    }

    class RangeSetting(name: String, value: Int, private val minValue: Int, private val maxValue: Int): Setting<Int>(name, value) {
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
    }

    class BooleanSetting(name: String, value: Boolean): Setting<Boolean>(name, value) {
        override fun onActivated() {
            value = !value
        }
    }

    init {
        init(this)
    }

    fun rangeSetting(name: String, value: Int, minValue: Int, maxValue: Int): RangeSetting {
        val setting = RangeSetting(name, value, minValue, maxValue)
        add(setting)
        return setting
    }

    fun booleanSetting(name: String, value: Boolean): BooleanSetting {
        val setting = BooleanSetting(name, value)
        add(setting)
        return setting
    }
}