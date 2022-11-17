package com.mojang.escape.menu

import com.mojang.escape.col
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.lang.IStringUnit
import com.mojang.escape.lang.Language
import com.mojang.escape.menu.settings.GameSettings
import com.mojang.escape.toLiteral
import com.mojang.escape.toTranslatable

class LanguagesMenu(lastMenu: Menu? = null): ScrollableMenu(lastMenu) {
    private val languageNames = hashMapOf<String, IStringUnit>(
        "en_US" to "gui.languages.en_US".toTranslatable(),
        "fr" to "gui.languages.fr".toTranslatable()
    )
    private val languages = arrayOf(
        "en_US",
        "fr"
    )
    
    override val title: IStringUnit = "gui.menu.languages.title".toTranslatable()
    override val lastButton: IStringUnit = "gui.menu.languages.buttonBack".toTranslatable()
    override val numLines: Int = languages.size

    override fun drawLine(target: Bitmap, lineIndex: Int, selected: Boolean) {
        val str = (if (selected) "-> " else "").toLiteral() + languageNames[languages[lineIndex]]!!
        val col = (if (selected) 0xFFFF80 else 0xA0A0A0).col
        target.draw(str, 0, 0, col)
    }

    override fun lineActivated(lineIndex: Int) {
        GameSettings.language.value = Language(languages[lineIndex])
    }
}