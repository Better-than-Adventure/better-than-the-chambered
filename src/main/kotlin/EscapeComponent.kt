package com.mojang.escape

import com.mojang.escape.gui.Screen
import com.mojang.escape.level.wolf3d.GameMaps
import com.mojang.escape.level.wolf3d.MapHead
import java.awt.*
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.lang.Exception
import javax.swing.JFrame
import javax.swing.JPanel

class EscapeComponent: Canvas(), Runnable {
    companion object {
        private const val serialVersionUID = 1L

        private const val WIDTH = 160
        private const val HEIGHT = 120
        private const val SCALE = 4
        
        lateinit var wolf3D: GameMaps
    }

    private var running = false
    private lateinit var thread: Thread

    private val game: Game
    private val screen: Screen

    private val img: BufferedImage
    private val pixels: IntArray

    private val inputHandler: InputHandler

    private val emptyCursor: Cursor
    private val defaultCursor: Cursor

    private var hadFocus = false

    private var robot = Robot()

    init {
        val size = Dimension(WIDTH * SCALE, HEIGHT * SCALE)
        this.size = size
        this.preferredSize = size
        this.minimumSize = size
        this.maximumSize = size

        this.game = Game()
        Game.theGame = this.game
        this.screen = Screen(WIDTH, HEIGHT)

        this.img = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
        this.pixels = (img.raster.dataBuffer as DataBufferInt).data

        this.inputHandler = InputHandler()

        addKeyListener(inputHandler)
        addFocusListener(inputHandler)
        addMouseListener(inputHandler)
        addMouseMotionListener(inputHandler)

        emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), Point(0, 0), "empty")
        defaultCursor = cursor
        
        // Read Wolf3D
        EscapeComponent::class.java.getResourceAsStream("/wolf3d/MAPHEAD.WL6")!!.use { mapheadStream ->
            EscapeComponent::class.java.getResourceAsStream("/wolf3d/GAMEMAPS.WL6")!!.use { gamemapsStream ->
                val mapheadBytes = mapheadStream.readBytes()
                val gamemapsBytes = gamemapsStream.readBytes()
                
                val mapHead = MapHead(mapheadBytes)
                wolf3D = GameMaps(gamemapsBytes, mapHead)
            }
        }
        
        val gamemap = wolf3D.levelHeaders[0]
        println("\nPlane 0:")
        for (y in 0 until 64) {
            for (x in 0 until 64) {
                print(String.format("%4d ", gamemap.plane0!![x + y * 64].toInt()))
            }
            println()
        }
        println("\nPlane 1:")
        for (y in 0 until 64) {
            for (x in 0 until 64) {
                print(String.format("%4d ", gamemap.plane1!![x + y * 64].toInt()))
            }
            println()
        }
        println("\nPlane 2:")
        for (y in 0 until 64) {
            for (x in 0 until 64) {
                print(String.format("%4d ", gamemap.plane2!![x + y * 64].toInt()))
            }
            println()
        }
    }

    @Synchronized
    fun start() {
        if (running) {
            return
        }
        running = true
        thread = Thread(this)
        thread.start()
    }

    @Synchronized
    fun stop() {
        if (!running) {
            return
        }
        running = false
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        var frames = 0

        var unprocessedSeconds = 0.0
        var lastTime = System.nanoTime()
        val secondsPerTick = 1 / 60.0
        var tickCount = 0

        requestFocus()

        while (running) {
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

    private fun tick() {
        if (hasFocus()) {
            if (game.menu != null || !hasFocus()) {
                cursor = defaultCursor
            } else {
                cursor = emptyCursor
                robot.mouseMove(locationOnScreen.x + width / 2, locationOnScreen.y + height / 2)
            }
            game.tick(inputHandler.keys, Pair(inputHandler.mousePos.first - width / 2, inputHandler.mousePos.second - height / 2), inputHandler.mouseButtons)
        }
    }

    private fun render() {
        val bs = bufferStrategy
        if (bs == null) {
            createBufferStrategy(3)
            return
        }

        screen.render(game, hasFocus())

        System.arraycopy(screen.pixels, 0, pixels, 0, WIDTH * HEIGHT)

        val g = bs.drawGraphics
        g.fillRect(0, 0, width, height)
        g.drawImage(img, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null)
        g.dispose()
        bs.show()
    }
}

fun main() {
    val game = EscapeComponent()

    val frame = JFrame("Better than the Chambered!")

    val panel = JPanel(BorderLayout())
    panel.add(game, BorderLayout.CENTER)

    frame.contentPane = panel
    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.isResizable = false
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true

    game.start()
}