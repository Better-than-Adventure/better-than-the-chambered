package com.mojang.escape

import java.awt.event.*
import java.util.*

class InputHandler: KeyListener, FocusListener, MouseListener, MouseMotionListener {
    val keys = BooleanArray(65536)

    override fun mouseDragged(e: MouseEvent?) {
    }

    override fun mouseMoved(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun focusGained(e: FocusEvent?) {
    }

    override fun focusLost(e: FocusEvent?) {
        Arrays.fill(keys, false)
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            val code = e.keyCode
            if (code > 0 && code < keys.size) {
                keys[code] = true
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        if (e != null) {
            val code = e.keyCode
            if (code > 0 && code < keys.size) {
                keys[code] = false
            }
        }
        val kt: KeyEvent
    }

    override fun keyTyped(e: KeyEvent?) {
    }
}