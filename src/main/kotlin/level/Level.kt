package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.*
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.block.*
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.level.provider.PNGLevelProvider
import com.mojang.escape.level.provider.Wolf3DLevelProvider
import com.mojang.escape.level.wolf3d.GameMaps
import com.mojang.escape.menu.GotLootMenu
import com.mojang.escape.x
import com.mojang.escape.y
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.math.floor

open class Level(game: Game, levelProvider: ILevelProvider) {
    companion object {
        val loaded = hashMapOf<String, Level>()

        fun clear() {
            loaded.clear()
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
        val spawn = levelProvider.getSpawn(this)
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