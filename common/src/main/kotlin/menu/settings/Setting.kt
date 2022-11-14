package com.mojang.escape.menu.settings

abstract class BaseSetting<T>(val name: String, var value: T) {
    abstract val valueString: String
}

class BooleanSetting(name: String, value: Boolean): BaseSetting<Boolean>(name, value) {
    override val valueString: String
    get() {
        return value.toString().uppercase()
    }
}
