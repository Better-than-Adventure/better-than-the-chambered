package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.col
import com.mojang.escape.level.block.LadderBlock
import com.mojang.escape.toLiteral
import java.util.*
import kotlin.random.Random

class RandomLevel: Level() {
    companion object {
        val random = Random(System.currentTimeMillis())
        val nameAdjectives = arrayOf("Grisly", "Fetid", "Damp", "Frosty", "Decrepit", "Dusty", "Gangrenous", "Vile", "Endless", "Barren", "Putrid")
        val nameNouns = arrayOf("Grotto", "Tomb", "Caverns", "Ruins", "Warrens", "Crypt", "Catacombs", "Chamber", "Maze")

        fun loadRandomLevel(game: Game, name: String): Level {
            if (Level.loaded.containsKey(name)) {
                return loaded.getValue(name)
            }

            val level = RandomLevel()
            level.init(game, name, level.width, level.height, level.pixels)
            loaded[name] = level
            level.blocks[1 + 1 * level.width] = LadderBlock(false).apply {
                id = 1
                col = level.wallCol
                ceilCol = level.ceilCol
                floorCol = level.floorCol
                if (tex == -1) tex = level.wallTex
                if (floorTex == -1) floorTex = level.floorTex
                if (ceilTex == -1) ceilTex = level.ceilTex
                this.level = level
                x = 1
                y = 1
            }
            level.blocks[(level.width - 2) + (level.height - 2) * level.width] = LadderBlock(true).also {
                it.id = 2
                it.col = level.wallCol
                it.ceilCol = level.ceilCol
                it.floorCol = level.floorCol
                if (it.tex == -1) it.tex = level.wallTex
                if (it.floorTex == -1) it.floorTex = level.floorTex
                if (it.ceilTex == -1) it.ceilTex = level.ceilTex
                it.level = level
                it.x = level.width - 2
                it.y = level.height - 2
            }

            return level
        }
    }

    val pixels: IntArray

    init {
        width = 16 + random.nextInt(64)
        if (width % 2 == 0) width++
        if (height % 2 == 0) height++
        height = 16 + random.nextInt(64)
        wallCol = (random.nextInt(256) shl 16) or (random.nextInt(256) shl 8) or (random.nextInt(256) shl 0)
        floorCol = (random.nextInt(256) shl 16) or (random.nextInt(256) shl 8) or (random.nextInt(256) shl 0)
        ceilCol = (random.nextInt(256) shl 16) or (random.nextInt(256) shl 8) or (random.nextInt(256) shl 0)
        wallCol = wallCol.col
        floorCol = floorCol.col
        ceilCol = ceilCol.col
        name = ("The " + nameAdjectives.random(random) + " " + nameNouns.random(random)).toLiteral()
        pixels = IntArray(width * height) {
            0xFFFFFF
        }

        generatePixels()
        xSpawn = 1
        ySpawn = 1
    }

    private fun generatePixels() {
        val visited = BooleanArray((width / 2) * (height / 2))
        val stack = Stack<Pair<Int, Int>>()
        val xOffs = arrayOf(-1, +1,  0,  0)
        val yOffs = arrayOf( 0,  0, -1, +1)

        fun visited(x: Int, y: Int): Boolean {
            if (x < 0 || x >= (width / 2) || y < 0 || y >= (height / 2)) return true
            return visited[x + y * (width / 2)]
        }
        fun markVisited(x: Int, y: Int) {
            visited[x + y * (width / 2)] = true
        }
        fun clearPixel(x: Int, y: Int) {
            pixels[x + y * width] = 0x000000
        }

        stack.push(Pair(0, 0))
        markVisited(0, 0)
        while (stack.size > 0) {
            val currentCell = stack.pop()
            clearPixel(currentCell.first * 2 + 1, currentCell.second * 2 + 1)
            val unvisitedNeighbors = mutableListOf<Pair<Int, Int>>()
            for (i in 0..3) {
                val x = currentCell.first + xOffs[i]
                val y = currentCell.second + yOffs[i]
                if (!visited(x, y)) {
                    unvisitedNeighbors.add(Pair(x, y))
                }
            }
            if (unvisitedNeighbors.size > 0) {
                stack.push(currentCell)
                val neighbor = unvisitedNeighbors.random(random)
                val dx = neighbor.first - currentCell.first
                val dy = neighbor.second - currentCell.second
                clearPixel(currentCell.first * 2 + 1 + dx, currentCell.second * 2 + 1 + dy)
                markVisited(neighbor.first, neighbor.second)
                stack.push(neighbor)
            }
        }

        pixels[1 + 1 * width] = 0xFE66FE // Top left ladder up
        pixels[(width - 2) + (height - 2) * width] = 0x9D009E // Bottom right ladder down
    }

    override fun switchLevel(id: Int) {
        val levelId = nameId.substring(7).toInt()
        when (id) {
            1 -> game.switchLevel("random_" + (levelId - 1), 2)
            2 -> game.switchLevel("random_" + (levelId + 1), 1)
        }
    }
}