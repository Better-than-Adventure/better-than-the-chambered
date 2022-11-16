package com.mojang.escape.gui

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.palette.Palette
import com.mojang.escape.menu.settings.GameSettings
import com.mojang.escape.toLiteral
import com.mojang.escape.toTranslatable
import java.util.*
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Screen(width: Int, height: Int): Bitmap(width, height) {
    companion object {
        const val PANEL_HEIGHT = 29
    }

    private val testBitmap: Bitmap
    private val viewport: Bitmap3D

    init {
        viewport = Bitmap3D(width, height - PANEL_HEIGHT)

        val random = Random()
        testBitmap = Bitmap(64, 64)
        for (i in 0 until (64 * 64)) {
            testBitmap.pixels[i] = random.nextInt() * (random.nextInt(5) / 4)
        }
    }

    fun render(game: Game, hasFocus: Boolean) {
        if (game.level == null) {
            fill(0, 0, width, height, 0)
            if (game.menu != null) {
                game.menu!!.render(this)
            }
            postProcess(this, false)
        } else {
            val player = game.player!!
            val level = game.level!!
            val itemUsed = player.itemUseTime > 0
            val item = player.items[player.selectedSlot]

            if (game.pauseTime > 0) {
                fill(0, 0, width, height, 0)
                val message = "gui.ingame.enteringLevel".toTranslatable().format(level.name)
                message.draw(this, (width - message.length * 6) / 2, (viewport.height - 8) / 2 + 8 + 1, 0x111111)
                message.draw(this, (width - message.length * 6) / 2, (viewport.height - 8) / 2 + 8 + 1, 0x555544)
            } else {
                viewport.render(game)
                viewport.postProcess(level)

                val block = level.getBlock((player.x + 0.5).toInt(), (player.z + 0.5).toInt())
                if (block.messages != null && hasFocus) {
                    for (message in block.messages.withIndex()) {
                        message.value.draw(this, (width - message.value.length * 6) / 2, (viewport.height - block.messages.size * 8) / 2 + message.index * 8 + 1, 0x111111)
                        message.value.draw(this, (width - message.value.length * 6) / 2, (viewport.height - block.messages.size * 8) / 2 + message.index * 8, 0x555544)
                    }

                }

                postProcess(viewport, true)
                draw(viewport, 0, 0)
                var xx = (player.turnBob * 32).toInt()
                var yy = (sin(player.bobPhase * 0.4) * 1 * player.bob + player.bob * 2).toInt()

                if (itemUsed) {
                    xx = 0
                    yy = 0
                }
                xx += width / 2
                yy += height - PANEL_HEIGHT - 15 * 3
                if (item != Item.None) {
                    scaleDraw(Art.items, 3, xx, yy, 16 * item.icon + 1, 16 + 1 + (if (itemUsed) 16 else 0), 15, 15, Art.getCol(item.color))
                }

                if (player.hurtTime > 0 || player.dead) {
                    var offs = 1.5 - player.hurtTime / 30.0
                    val random = Random(111)
                    if (player.dead) offs = 0.5
                    for (i in pixels.indices) {
                        val xp = ((i % width) - viewport.width / 2.0) / width * 2
                        val yp = ((i / width) - viewport.height / 2.0) / viewport.height * 2

                        if (random.nextDouble() + offs < sqrt(xp * xp + yp * yp)) {
                            pixels[i] = (random.nextInt(5) / 4) * 0x550000
                        }
                    }
                }

                draw(Art.panel, 0, height - PANEL_HEIGHT, 0, 0, width, PANEL_HEIGHT, Art.getCol(0x707070))

                draw("symbols.key".toTranslatable(Game.symbols), 3, height - 26 + 0, 0x00ffff)
                draw(("" + player.keys + "/4").toLiteral(), 10, height - 26 + 0, 0xffffff)
                draw("symbols.trophy".toTranslatable(Game.symbols), 3, height - 26 + 8, 0xffff00)
                draw(("" + player.loot).toLiteral(), 10, height - 26 + 8, 0xffffff)
                draw("symbols.heart".toTranslatable(Game.symbols), 3, height - 26 + 16, 0xff0000)
                draw(("" + player.health).toLiteral(), 10, height - 26 + 16, 0xffffff)

                for (i in 0 until 8) {
                    val slotItem = player.items[i]
                    if (slotItem != Item.None) {
                        draw(Art.items, 30 + i * 16, height - PANEL_HEIGHT + 2, slotItem.icon * 16, 0, 16, 16, Art.getCol(slotItem.color))
                        if (slotItem == Item.Pistol) {
                            val str = "" + player.ammo
                            draw(str.toLiteral(), 30 + i * 16 + 17 - str.length * 6, height - PANEL_HEIGHT + 1 + 10, 0x555555)
                        }
                        if (slotItem == Item.Potion) {
                            val str = "" + player.potions
                            draw(str.toLiteral(), 30 + i * 16 + 17 - str.length * 6, height - PANEL_HEIGHT + 1 + 10, 0x555555)
                        }
                    }
                }

                draw(Art.items, 30 + player.selectedSlot * 16, height - PANEL_HEIGHT + 2, 0, 48, 17, 17, Art.getCol(0xFFFFFF))
                item.itemName.draw(this, 26 + (8 * 16 - item.itemName.length * 4) / 2, height - 9, 0xFFFFFF)
            }

            if (game.menu != null) {
                for (i in pixels.indices) {
                    pixels[i] = (pixels[i] and 0xFCFCFC) shr 2
                }
                game.menu!!.render(this)
            }

            if (!hasFocus) {
                for (i in pixels.indices) {
                    pixels[i] = (pixels[i] and 0xFCFCFC) shr 2
                }
                if (System.currentTimeMillis() / 450 % 2 != 0L) {
                    val msg = "gui.ingame.focusLost".toTranslatable()
                    draw(msg, (width - msg.length * 6) / 2, height / 3 + 4, 0xFFFFFF)
                }
            }

            postProcess(this, false)
        }
    }

    fun postProcess(bitmap: Bitmap, dither: Boolean) {
        val MAX_LEVEL = 4

        fun distance(a: Triple<Double, Double, Double>, b: Triple<Double, Double, Double>): Double {
            return (b.first - a.first).pow(2) + (b.second - a.second).pow(2) + (b.third - a.third).pow(2)
        }
        fun bayer2x2(x: Int, y: Int): Int {
            return (4 - x - (y shl 1)) % 4
        }
        fun bayer(x: Int, y: Int): Float {
            var sum = 0
            for (i in 0 until MAX_LEVEL) {
                sum += bayer2x2(x shr (MAX_LEVEL - 1 - i) and 1, y shr (MAX_LEVEL - 1 - i) and 1) shl (2 * i)
            }
            return sum.toFloat() / (2 shl (MAX_LEVEL * 2 - 1)).toFloat()
        }

        val colorDetail = 1.0

        if (GameSettings.graphics.value > 0) {
            // Shamelessly copied from https://www.shadertoy.com/view/4dXSzl
            for (i in bitmap.pixels.indices) {
                val x = i / bitmap.width
                val y = i % bitmap.width

                // 0x00, 0x55, 0xAA, 0xFF
                val pixel = bitmap.pixels[i]
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr  8) and 0xFF
                val b = (pixel shr  0) and 0xFF
                var s = Triple(r / 255.0, g / 255.0, b / 255.0)
                if (dither) {
                    val bayer = bayer(x, y)
                    s = Triple(s.first + (bayer - 0.5) * 0.5, s.second + (bayer - 0.5) * 0.5, s.third + (bayer - 0.5) * 0.5)
                }
                if (GameSettings.graphics.value == 1) {
                    val blend = Triple(floor(s.first * colorDetail + 0.5) / colorDetail, floor(s.second * colorDetail + 0.5) / colorDetail, floor(s.third * colorDetail + 0.5) / colorDetail)
                    s = Triple(s.first * 0.6 + blend.first * 0.4, s.second * 0.6 + blend.second * 0.4, s.third * 0.6 + blend.third * 0.4)
                }

                var dist = 0.0
                var bestDistance = 1000.0
                var bestColor = Triple(0.0, 0.0, 0.0)
                fun check(r: Double, g: Double, b: Double) {
                    val color = Triple(r, g, b)
                    dist = distance(s, color)
                    if (dist < bestDistance) {
                        bestDistance = dist
                        bestColor = color
                    }
                }

                val palette = when (GameSettings.graphics.value) {
                    2 -> Palette.cga
                    else -> Palette.ega
                }
                for (color in palette.iterator()) {
                    check(color.first / 255.0, color.second / 255.0, color.third / 255.0)
                }

                bitmap.pixels[i] = ((bestColor.first * 255).toInt() shl 16) or ((bestColor.second * 255).toInt() shl 8) or ((bestColor.third * 255).toInt() shl 0)
            }
        }
    }
}