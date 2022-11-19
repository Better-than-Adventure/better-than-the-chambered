package com.mojang.escape

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack

class InputHandler {
    private val lastStates = IntArray(Keys.values().size) { GLFW_RELEASE }
    val keys = BooleanArray(Keys.values().size)
    private var lastMousePos = Pair(0.0, 0.0)
    var mousePos = Pair(0.0, 0.0)
    
    fun updateKeys() {
        for (key in Keys.values()) {
            val state = glfwGetKey(Escape.window, key.code)
            if (state == GLFW_PRESS && lastStates[key.ordinal] == GLFW_RELEASE) {
                keys[key.ordinal] = true
            } else if (state == GLFW_RELEASE && lastStates[key.ordinal] == GLFW_PRESS) {
                keys[key.ordinal] = false
            }
            lastStates[key.ordinal] = state
        }
    }
    
    fun updateMouse() {
        MemoryStack.stackPush().use { stack -> 
            val x = stack.mallocDouble(1)
            val y = stack.mallocDouble(1)
            
            glfwGetCursorPos(Escape.window, x, y)
            
            mousePos = Pair(x[0] - lastMousePos.first, y[0] - lastMousePos.second)
            lastMousePos = Pair(x[0], y[0])
        }
    }
}