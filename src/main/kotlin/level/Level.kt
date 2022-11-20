package com.mojang.escape.level

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.entities.*
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.lang.StringUnitTranslatable
import com.mojang.escape.level.block.*
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.level.provider.PNGLevelProvider
import com.mojang.escape.menu.GotLootMenu
import com.mojang.escape.util.Image
import com.mojang.escape.x
import com.mojang.escape.y
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.math.floor

abstract class Level(game: Game, levelProvider: ILevelProvider) {
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
                val levelProvider = PNGLevelProvider(
                    name,
                    wallCol = when (name) {
                        "crypt" -> 0x404040
                        "dungeon" -> 0xC64954
                        "ice" -> 0x6BE8FF
                        "overworld" -> 0xA0A0A0
                        "temple" -> 0xCFADD8
                        else -> 0xB3CEE2
                    },
                    floorCol = when (name) {
                        "crypt" -> 0x404040
                        "dungeon" -> 0x8E4A51
                        "ice" -> 0xB8DBE0
                        "overworld" -> 0x508253
                        "temple" -> 0x8A6496
                        else -> 0x9CA09B
                    },
                    ceilCol = when (name) {
                        "crypt" -> 0x404040
                        "dungeon" -> 0x8E4A51
                        "ice" -> 0xB8DBE0
                        "temple" -> 0x8A6496
                        else -> 0x9CA09B
                    },
                    wallTex = when (name) {
                        else -> 0
                    },
                    floorTex = when (name) {
                        "overworld" -> 8 + 3
                        else -> 0
                    },
                    ceilTex = when (name) {
                        "overworld" -> -1
                        else -> 0
                    }
                )

                val level = Level.byName(name, game, levelProvider)
                level.postInit()
                loaded[name] = level

                return level
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        private fun byName(name: String, game: Game, levelProvider: ILevelProvider): Level {
            try {
                val className = name.substring(0, 1).uppercase() + name.substring(1) + "Level"
                return Class.forName("com.mojang.escape.level.$className").constructors[0].newInstance(game, levelProvider) as Level
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    var blocks: Array<Block>

    var width = 0
    var height = 0

    val solidWall = SolidBlock()

    var xSpawn: Int
    var ySpawn: Int

    val wallCol: Int
    val floorCol: Int
    val ceilCol: Int

    val wallTex: Int
    val floorTex: Int
    val ceilTex: Int

    val entities: MutableList<Entity>
    val game: Game
    val name: StringUnit

    lateinit var player: Player
    
    init {
        this.game = game
        
        this.name = levelProvider.getName()
        this.wallCol = levelProvider.getWallCol()
        this.wallTex = levelProvider.getWallTex()
        this.floorCol = levelProvider.getFloorCol()
        this.floorTex = levelProvider.getFloorTex()
        this.ceilCol = levelProvider.getCeilCol()
        this.ceilTex = levelProvider.getCeilTex()
        this.width = levelProvider.getWidth()
        this.height = levelProvider.getHeight()
        this.blocks = levelProvider.getBlocks(this)
        this.entities = levelProvider.getEntities(this)
        val spawn = levelProvider.getSpawn()
        if (spawn != null) {
            xSpawn = spawn.x
            ySpawn = spawn.y
        } else {
            xSpawn = 0
            ySpawn = 0
        }
    }
    
    open fun postInit() {
        // Do nothing
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