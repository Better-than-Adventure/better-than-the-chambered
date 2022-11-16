package com.mojang.escape.lang

interface ITranslatable {
    val translated: String

    val length: Int
        get() = translated.length

    infix fun and(other: ITranslatable): TranslatableGroup {
        return TranslatableGroup(this, other)
    }

    infix fun and(other: String): TranslatableGroup {
        return TranslatableGroup(this, other)
    }

    fun format(vararg params: ITranslatable): TranslatableFormatted {
        return TranslatableFormatted(this, *params)
    }
}