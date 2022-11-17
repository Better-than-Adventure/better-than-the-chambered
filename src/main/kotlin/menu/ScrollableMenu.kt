package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.lang.IStringUnit

abstract class ScrollableMenu(lastMenu: Menu? = null): Menu(lastMenu) {
    companion object {
        const val LINES_ON_SCREEN = 6
    }

    private var selected = 0
    private var scroll = 0
    
    protected abstract val title: IStringUnit
    protected abstract val lastButton: IStringUnit
    
    protected abstract val numLines: Int

    override fun render(target: Bitmap) {
        target.draw(title, 40, 8, Art.getCol(0xFFFFFF))

        val scrollProgress = scroll.toDouble() / (numLines - LINES_ON_SCREEN).toDouble()
        val scrollbarY = scrollProgress * (LINES_ON_SCREEN - 1) * 8
        target.fill(target.width - 6, 32, target.width - 1, 32 + LINES_ON_SCREEN * 8 - 1, 0x505050.col)
        target.draw("symbols.scrollbar".toTranslatable(Game.symbols), target.width - 6, 32 + scrollbarY.toInt(), 0xA0A0A0.col)

        val linesToDraw = if (LINES_ON_SCREEN < numLines) LINES_ON_SCREEN else numLines  
        
        for (index in scroll until (scroll + linesToDraw)) {
            val bitmap = Bitmap(target.width - 8 - 4, 8)
            drawLine(bitmap, index, index == selected)
            target.draw(bitmap, 4, 32 + (index - scroll) * 8)
        }

        val str = (if (selected == -1) "-> " else "   ").toLiteral() + lastButton
        target.draw(str, 40, target.height - 32, (if (selected == -1) 0xFFFF80 else 0xA0A0A0).col)
    }
    
    protected abstract fun drawLine(target: Bitmap, lineIndex: Int, selected: Boolean)
    
    protected abstract fun lineActivated(lineIndex: Int)

    override fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (up) {
            if (selected == -1) {
                selected = numLines - 1
            } else if (selected > 0) {
                selected--
            } else {
                selected = -1
            }
        }
        if (down) {
            if (selected == -1) {
                selected = 0
            } else if (selected < numLines - 1) {
                selected++
            } else {
                selected = -1
            }
        }
        if (left || right) {
            selected = -1
        }
        if (use) {
            Sound.click1.play()
            if (selected == -1) {
                game.menu = lastMenu ?: TitleMenu()
            } else {
                lineActivated(selected)
            }
        }

        if (selected != -1) {
            if (scroll > selected) {
                scroll = selected
            }
            if (scroll + LINES_ON_SCREEN <= selected) {
                scroll = selected - LINES_ON_SCREEN + 1
            }
        }
    }
}