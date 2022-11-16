package com.mojang.escape.util

import com.mojang.escape.Game

class TranslatedString(val key: String) {
    val value: String
        get() = Game.lang.strings.getProperty(key) ?: key
}