package com.mojang.escape.lang

import com.mojang.escape.Game

class Translatable(private val key: String, private val lang: Language? = null): ITranslatable {
    override val translated: String
        get() {
            return (lang ?: Game.lang).strings.getProperty(key) ?: key
        }
}