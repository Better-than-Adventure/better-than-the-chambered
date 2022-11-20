package com.mojang.escape

import com.mojang.escape.gui.Screen
import com.mojang.escape.render.Display
import jdk.internal.util.xml.impl.Input
import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.opengl.*

import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL43.*
import org.lwjgl.system.MemoryStack.*
import org.lwjgl.system.MemoryUtil.*
import java.lang.IllegalStateException
import java.lang.RuntimeException
import kotlin.math.min


class Escape {
    companion object {
        const val WIDTH = 160
        const val HEIGHT = 120
        const val SCALE = 4
        
        var window = -1L
    }
    
    private lateinit var game: Game
    private lateinit var screen: Screen
    
    private val display = Display()
    
    private val inputHandler: InputHandler = InputHandler()
    
    fun run() {
        initGLFW()
        initGame()
        loop()
        
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }
    
    fun initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set()
        
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW!")
        }
        
        glfwDefaultWindowHints()
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(WIDTH * SCALE, HEIGHT * SCALE, "Better than the Chambered!", NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window!")
        }
        
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            
            glfwGetWindowSize(window, pWidth, pHeight)
            
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
            val screenWidth = vidmode?.width()
            val screenHeight = vidmode?.height()
            
            val windowX = if (screenWidth != null) {
                (vidmode.width() - pWidth[0]) / 2
            } else {
                0
            }
            val windowY = if (screenHeight != null) {
                (vidmode.height() - pHeight[0]) / 2
            } else {
                0
            }
            
            glfwSetWindowPos(window, windowX, windowY)
        }
        
        glfwMakeContextCurrent(window)
        
        glfwShowWindow(window)
        
        // Try to do raw mouse input
        if (glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
        }

        glfwSetWindowSizeCallback(window) { _, width, height ->
            val widthScale = width / WIDTH.toDouble()
            val heightScale = height / HEIGHT.toDouble()
            val scale = min(widthScale, heightScale)
            val vpWidth = WIDTH * scale
            val vpHeight = HEIGHT * scale
            val vpX = width / 2 - vpWidth / 2
            val vpY = height / 2 - vpHeight / 2
            glViewport(vpX.toInt(), vpY.toInt(), vpWidth.toInt(), vpHeight.toInt())
        }
    }
    
    fun initGame() {
        this.game = Game()
        Game.theGame = this.game
        this.screen = Screen(WIDTH, HEIGHT)
    }
    
    fun loop() {
        GL.createCapabilities()
        
        glEnable(GL_DEBUG_OUTPUT)
        if (GL.getCapabilities().OpenGL43) {
            glDebugMessageCallback({ _, type, _, severity, length, message, _ ->
                println(String.format("GL CALLBACK:%s type 0x%X, severity = 0x%X, message = %s",
                    (if (type == GL_DEBUG_TYPE_ERROR) " ** GL ERROR **" else ""),
                    type, severity, GLDebugMessageCallback.getMessage(length, message))
                )
            }, 0)
        }

        display.init()
        display.bitmap = screen

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        
        var frames = 0
        var unprocessedSeconds = 0.0
        var lastTime = System.nanoTime()
        val secondsPerTick = 1 / 60.0
        var tickCount = 0

        while (!glfwWindowShouldClose(window)) {
            val now = System.nanoTime()
            var passedTime = now - lastTime
            lastTime = now
            if (passedTime < 0) {
                passedTime = 0
            }
            if (passedTime > 100000000) {
                passedTime = 100000000
            }

            unprocessedSeconds += passedTime / 1000000000.0
            
            inputHandler.updateKeys()

            var ticked = false
            while (unprocessedSeconds > secondsPerTick) {
                tick()
                unprocessedSeconds -= secondsPerTick
                ticked = true

                tickCount++
                if (tickCount % 60 == 0) {
                    println("$frames fps")
                    lastTime += 1000
                    frames = 0
                }
            }

            if (ticked) {
                render()
                frames++
            } else {
                try {
                    Thread.sleep(1)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun tick() {
        if (glfwGetWindowAttrib(window, GLFW_FOCUSED) == 1) {
            if (game.menu != null) {
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
            } else {
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
            }
            inputHandler.updateMouse()
            game.tick(inputHandler.keys, inputHandler.mousePos, inputHandler.mouseButtons)
        }
    }
    
    fun render() {
        screen.render(game, true)

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        display.render()

        glfwSwapBuffers(window)

        glfwPollEvents()
    }
}

fun main() {
    Escape().run()
}