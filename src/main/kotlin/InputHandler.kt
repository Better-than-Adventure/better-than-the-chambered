package com.mojang.escape

import java.awt.MouseInfo
import java.awt.event.*
import java.util.*

class InputHandler: KeyListener, FocusListener, MouseListener, MouseMotionListener {
    val keys = BooleanArray(Keys.values().size)
    var mousePos = Pair(0, 0)
    var mouseButtons = BooleanArray(MouseInfo.getNumberOfButtons())

    override fun mouseDragged(e: MouseEvent?) {
        mousePos = Pair(e!!.x, e.y)
    }

    override fun mouseMoved(e: MouseEvent?) {
        mousePos = Pair(e!!.x, e.y)
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
        mouseButtons[e!!.button - 1] = true
    }

    override fun mouseReleased(e: MouseEvent?) {
        mouseButtons[e!!.button - 1] = false
    }

    override fun focusGained(e: FocusEvent?) {
    }

    override fun focusLost(e: FocusEvent?) {
        Arrays.fill(keys, false)
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            for (key in Keys.values()) {
                if (key.code == e.keyCode) {
                    keys[key.ordinal] = true
                    break
                }
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        if (e != null) {
            for (key in Keys.values()) {
                if (key.code == e.keyCode) {
                    keys[key.ordinal] = false
                    break
                }
            }
        }
    }

    override fun keyTyped(e: KeyEvent?) {
    }
}