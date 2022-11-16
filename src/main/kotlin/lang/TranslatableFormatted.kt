package com.mojang.escape.lang

class TranslatableFormatted(private val translatable: ITranslatable, private vararg val params: ITranslatable): ITranslatable {
    override val translated: String
        get() {
            var str = translatable.translated
            for (param in params) {
                str = str.replaceFirst("%s", param.translated)
            }
            return str
        }
}