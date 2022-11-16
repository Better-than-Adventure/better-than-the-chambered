package com.mojang.escape.level

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.entities.*
import com.mojang.escape.lang.StringUnitTranslatable
import com.mojang.escape.level.block.*
import com.mojang.escape.menu.GotLootMenu
import java.lang.Exception
import java.lang.RuntimeException
import javax.imageio.ImageIO
import kotlin.math.floor

abstract class Level {
    companion object {
        val loaded = hashMapOf<String, Level>()

        fun clear() {
            loaded.clear()
        }

        fun loadLevel(game: Game, name: String): Level {
            if (loaded.containsKey(name)) {
                return loaded.getValue(name)
            }

            try {
                val img = ImageIO.read(Level::class.java.getResource("/level/$name.png"))

                val w = img.width
                val h = img.height
                val pixels = IntArray(w * h)
                img.getRGB(0, 0, w, h, pixels, 0, w)

                val level = Level.byName(name)
                level.init(game, name, w, h, pixels)
                loaded[name] = level

                return level
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        private fun byName(name: String): Level {
            try {
                val className = name.substring(0, 1).uppercase() + name.substring(1) + "Level"
                return Class.forName("com.mojang.escape.level.$className").newInstance() as Level
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    lateinit var blocks: Array<Block>

    var width = 0
    var height = 0

    private val solidWall = SolidBlock()

    var xSpawn = 0
    var ySpawn = 0

    var wallCol = 0xB3CEE2
    var floorCol = 0x9CA09B
    var ceilCol = 0x9CA09B

    var wallTex = 0
    var floorTex = 0
    var ceilTex = 0

    val entities = mutableListOf<Entity>()
    lateinit var game: Game
    var name = StringUnitTranslatable("")

    lateinit var player: Player

    open fun init(game: Game, name: String, w: Int, h: Int, pixels: IntArray) {
        this.game = game

        solidWall.col = Art.getCol(wallCol)
        solidWall.tex = Art.getCol(wallTex)
        width = w
        height = h
        blocks = Array(width * height) {
            val x = it % width
            val y = it / width

            val col = pixels[x + y * w] and 0xFFFFFF
            val id = 255 - ((pixels[x + y * w] shr 24) and 0xFF)

            val block = getBlock(x, y, col)
            block.id = id

            if (block.tex == -1) {
                block.tex = wallTex
            }
            if (block.floorTex == -1) {
                block.floorTex = floorTex
            }
            if (block.ceilTex == -1) {
                block.ceilTex = ceilTex
            }
            if (block.col == -1) {
                block.col = Art.getCol(wallCol)
            }
            if (block.floorCol == -1) {
                block.floorCol = Art.getCol(floorCol)
            }
            if (block.ceilCol == -1) {
                block.ceilCol = Art.getCol(ceilCol)
            }

            block.level = this
            block.x = x
            block.y = y

            block
        }

        for (y in 0 until h) {
            for (x in 0 until w) {
                val col = pixels[x + y * w] and 0xFFFFFF
                decorateBlock(x, y, blocks[x + y * w], col)
            }
        }
    }

    fun addEntity(e: Entity) {
        entities.add(e)
        e.level = this
        e.updatePos()
    }

    fun removeEntityImmediately(player: Player) {
        entities.remove(player)
        getBlock(player.xTileO, player.zTileO).removeEntity(player)
    }

    protected fun decorateBlock(x: Int, y: Int, block: Block, col: Int) {
        block.decorate(this, x, y)

        when (col) {
            0xFFFF00 -> {
                xSpawn = x
                ySpawn = y
            }
            0x1A2108 -> {
                block.floorTex = 7
                block.ceilTex = 7
            }
            0xC6C6C6, 0xC6C697 -> block.col = Art.getCol(0xA0A0A0)
            0x653A00 -> {
                block.floorCol = Art.getCol(0xB56600)
                block.floorTex = 3 * 8 + 1
            }
            0x93FF9B -> {
                block.col = Art.getCol(0x2AAF33)
                block.tex = 8
            }
            0xAA5500 -> addEntity(BoulderEntity(x, y))
            0xFF0000 -> addEntity(BatEntity(x.toDouble(), y.toDouble()))
            0xFF0001 -> addEntity(BatBossEntity(x.toDouble(), y.toDouble()))
            0xFF0002 -> addEntity(OgreEntity(x.toDouble(), y.toDouble()))
            0xFF0003 -> addEntity(OgreBossEntity(x.toDouble(), y.toDouble()))
            0xFF0004 -> addEntity(EyeEntity(x.toDouble(), y.toDouble()))
            0xFF0005 -> addEntity(EyeBossEntity(x.toDouble(), y.toDouble()))
            0xFF0006 -> addEntity(GhostEntity(x.toDouble(), y.toDouble()))
            0xFF0007 -> {
                addEntity(GhostBossEntity(x.toDouble(), y.toDouble()))
                block.floorTex = 7
                block.ceilTex = 7
            }
        }
    }

    protected fun getBlock(x: Int, y: Int, col: Int): Block {
        return when (col) {
            0x93FF9B -> SolidBlock()
            0x009300 -> PitBlock()
            0xFFFFFF -> SolidBlock()
            0x00FFFF -> VanishBlock()
            0xFFFF64 -> ChestBlock()
            0x0000FF -> WaterBlock()
            0xFF3A02 -> TorchBlock()
            0x4C4C4C -> BarsBlock()
            0xFF66FF -> LadderBlock(false)
            0x9E009E -> LadderBlock(true)
            0xC1C14D -> LootBlock()
            0xC6C6C6 -> DoorBlock()
            0x00FFA7 -> SwitchBlock()
            0x009380 -> PressurePlateBlock()
            0xFF0005 -> IceBlock()
            0x3F3F60 -> IceBlock()
            0xC6C697 -> LockedDoorBlock()
            0xFFBA02 -> AltarBlock()
            0x1A2108 -> Block()
            0x00C2A7 -> FinalUnlockBlock()
            0x000056 -> WinBlock()
            else -> Block()
        }
    }

    fun getBlock(x: Int, y: Int): Block {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return solidWall
        }

        return blocks[x + y * width]
    }

    fun containsBlockingEntity(x0: Double, y0: Double, x1: Double, y1: Double): Boolean {
        val xc = floor((x1 + x0) / 2).toInt()
        val zc = floor((y1 + y0) / 2).toInt()
        val rr = 2
        for (z in (zc - rr)..(zc + rr)) {
            for (x in (xc - rr)..(xc + rr)) {
                val es = getBlock(x, z).entities
                for (e in es) {
                    if (e.isInside(x0, y0, x1, y1)) return true
                }
            }
        }

        return false
    }

    fun containsBlockingNonFlyingEntity(x0: Double, y0: Double, x1: Double, y1: Double): Boolean {
        val xc = floor((x1 + x0) / 2).toInt()
        val zc = floor((y1 + y0) / 2).toInt()
        val rr = 2
        for (z in (zc - rr)..(zc + rr)) {
            for (x in (xc - rr)..(xc + rr)) {
                val es = getBlock(x, z).entities
                for (e in es) {
                    if (!e.flying && e.isInside(x0, y0, x1, y1)) return true
                }
            }
        }

        return false
    }

    fun tick() {
        var i = 0
        while (i < entities.size) {
            val e = entities[i]
            e.tick()
            e.updatePos()
            if (e.isRemoved()) {
                entities.removeAt(i--)
            }
            i++
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                blocks[x + y * width].tick()
            }
        }
    }

    open fun trigger(id: Int, pressed: Boolean) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val b = blocks[x + y * width]
                if (b.id == id) {
                    b.trigger(pressed)
                }
            }
        }
    }

    open fun switchLevel(id: Int) {
    }

    fun findSpawn(id: Int) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val b = blocks[x + y * width]
                if (b.id == id && b is LadderBlock) {
                    xSpawn = x
                    ySpawn = y
                }
            }
        }
    }

    open fun getLoot(id: Int) {
        if (id == 20) {
            game.getLoot(Item.Pistol)
        }
        if (id == 21) {
            game.getLoot(Item.Potion)
        }
    }

    fun win() {
        game.win(player)
    }

    fun lose() {
        game.lose(player)
    }

    fun showLootScreen(item: Item) {
        game.menu = GotLootMenu(item)
    }
}