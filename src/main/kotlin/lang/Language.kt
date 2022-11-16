package com.mojang.escape.lang

import com.mojang.escape.Art
import com.mojang.escape.util.StructuredProperties
import java.util.Properties

class Language(val name: String) {
    val fontBitmap = Art.loadBitmap("/lang/$name/font.png")
    val fontString = Language::class.java.getResource("/lang/$name/font.txt")!!.readText()
    val strings: Properties = StructuredProperties()

    init {
        strings.load(Language::class.java.getResourceAsStream("/lang/$name/strings.lang"))
    }
}