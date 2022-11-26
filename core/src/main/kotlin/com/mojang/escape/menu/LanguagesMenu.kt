package com.mojang.escape.menu

import com.mojang.escape.Game
import com.mojang.escape.col
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.lang.Language
import com.mojang.escape.menu.settings.GameSettings
import com.mojang.escape.toLiteral
import com.mojang.escape.toTranslatable

class LanguagesMenu(lastMenu: Menu? = null): ScrollableMenu(lastMenu) {
    private val languageNames = hashMapOf<String, StringUnit>(
        "en_US" to "gui.languages.en_US".toTranslatable(),
        "fr" to "gui.languages.fr".toTranslatable()
    )
    private val languages = arrayOf(
        "en_US",
        "fr"
    )
    private val oldLanguage = GameSettings.language.value
    
    override val title: StringUnit = "gui.menu.languages.title".toTranslatable()
    override val lastButton: StringUnit = "gui.menu.languages.buttonBack".toTranslatable()
    override val numLines: Int = languages.size

    override fun drawLine(target: Bitmap, index: Int, selected: Boolean) {
        val str = (if (selected) "-> " else "").toLiteral() + languageNames[languages[index]]!!
        val col = (if (selected) 0xFFFF80 else 0xA0A0A0).col
        target.draw(str, 0, 0, col)
    }

    override fun onLineActivated(game: Game) {
        GameSettings.language.value = Language(languages[selected])
    }

    override fun onLastButtonActivated(game: Game) {
        onLineActivated(game)
    }

    override fun onCancel(game: Game) {
        GameSettings.language.value = oldLanguage
    }

    override fun onTick(game: Game) {
        // Do nothing
    }
}