package com.mojang.escape.lang

class TranslatableGroup(vararg params: Any): ITranslatable {
    private val parts = mutableListOf<Any>()

    override val translated: String
        get() {
            var buffer = ""
            for (part in parts) {
                if (part is String) {
                    buffer += part
                } else if (part is ITranslatable) {
                    buffer += part.translated
                }
            }
            return buffer
        }

    init {
        for (param in params) {
            if (param is String || param is ITranslatable) {
                if (param is TranslatableGroup) {
                    parts.addAll(param.parts)
                } else {
                    parts.add(param)
                }
            }
        }
    }
}