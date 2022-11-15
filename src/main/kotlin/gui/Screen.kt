package com.mojang.escape.gui

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.closest
import com.mojang.escape.entities.Item
import com.mojang.escape.menu.SettingsMenu
import com.mojang.escape.menu.settings.GameSettings
import java.util.*
import kotlin.math.sin
import kotlin.math.sqrt

class Screen(width: Int, height: Int): Bitmap(width, height) {
    companion object {
        const val PANEL_HEIGHT = 29
        private val ega = arrayOf(0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xAA5500, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF)
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
        } else {
            val player = game.player!!
            val level = game.level!!
            val itemUsed = player.itemUseTime > 0
            val item = player.items[player.selectedSlot]

            if (game.pauseTime > 0) {
                fill(0, 0, width, height, 0)
                val messages = arrayOf("Entering ${level.name}")
                for (message in messages.withIndex()) {
                    draw(message.value, (width - message.value.length * 6) / 2, (viewport.height - messages.size * 8) / 2 + message.index * 8 + 1, 0x111111)
                    draw(message.value, (width - message.value.length * 6) / 2, (viewport.height - messages.size * 8) / 2 + message.index * 8, 0x555544)
                }
            } else {
                viewport.render(game)
                viewport.postProcess(level)

                val block = level.getBlock((player.x + 0.5).toInt(), (player.z + 0.5).toInt())
                if (block.messages != null && hasFocus) {
                    for (message in block.messages.withIndex()) {
                        draw(message.value, (width - message.value.length * 6) / 2, (viewport.height - block.messages.size * 8) / 2 + message.index * 8 + 1, 0x111111)
                        draw(message.value, (width - message.value.length * 6) / 2, (viewport.height - block.messages.size * 8) / 2 + message.index * 8, 0x555544)
                    }

                }

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

                draw("å", 3, height - 26 + 0, 0x00ffff)
                draw("" + player.keys + "/4", 10, height - 26 + 0, 0xffffff)
                draw("Ä", 3, height - 26 + 8, 0xffff00)
                draw("" + player.loot, 10, height - 26 + 8, 0xffffff)
                draw("Å", 3, height - 26 + 16, 0xff0000)
                draw("" + player.health, 10, height - 26 + 16, 0xffffff)

                for (i in 0 until 8) {
                    val slotItem = player.items[i]
                    if (slotItem != Item.None) {
                        draw(Art.items, 30 + i * 16, height - PANEL_HEIGHT + 2, slotItem.icon * 16, 0, 16, 16, Art.getCol(slotItem.color))
                        if (slotItem == Item.Pistol) {
                            val str = "" + player.ammo
                            draw(str, 30 + i * 16 + 17 - str.length * 6, height - PANEL_HEIGHT + 1 + 10, 0x555555)
                        }
                        if (slotItem == Item.Potion) {
                            val str = "" + player.potions
                            draw(str, 30 + i * 16 + 17 - str.length * 6, height - PANEL_HEIGHT + 1 + 10, 0x555555)
                        }
                    }
                }

                draw(Art.items, 30 + player.selectedSlot * 16, height - PANEL_HEIGHT + 2, 0, 48, 17, 17, Art.getCol(0xFFFFFF))
                draw(item.itemName, 26 + (8 * 16 - item.itemName.length * 4) / 2, height - 9, 0xFFFFFF)
            }

            if (game.menu != null) {
                for (i in pixels.indices) {
                    pixels[i] = (pixels[i] and 0xFCFCFC) shr 2
                }
                game.menu!!.render(this)
            }

//            if (!hasFocus) {
//                for (i in pixels.indices) {
//                    pixels[i] = (pixels[i] and 0xFCFCFC) shr 2
//                }
//                if (System.currentTimeMillis() / 450 % 2 != 0L) {
//                    val msg = "Click to focus!"
//                    draw(msg, (width - msg.length * 6) / 2, height / 3 + 4, 0xFFFFFF)
//                }
//            }
        }
        postProcess(this)
    }

    fun postProcess(bitmap: Bitmap) {
        if (GameSettings.graphics == 1 || GameSettings.graphics == 2) {
            for (i in bitmap.pixels.indices) {
                val x = i / bitmap.width
                val y = i % bitmap.width
                val alternate = (((x % 2) + (y % 2)) % 2) == 1
                val ega = arrayOf(0x00, 0x55, 0xAA, 0xFF)

                // 0x00, 0x55, 0xAA, 0xFF
                val pixel = bitmap.pixels[i]
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr  8) and 0xFF
                val b = (pixel shr  0) and 0xFF
                var nr = ega[((r / 255.0) * (ega.size - 1)).toInt()]
                var ng = ega[((g / 255.0) * (ega.size - 1)).toInt()]
                var nb = ega[((b / 255.0) * (ega.size - 1)).toInt()]

                if (GameSettings.graphics == 2) {
                    var higher = false
                    if ((nr == 0xFF || ng == 0xFF || nb == 0xFF) || ((nr == 0xAA && ng == 0xAA) || (nr == 0xAA && nb == 0xAA) || (ng == 0xAA && nb == 0xAA))) {
                        higher = true
                    }
                    if (higher) {
                        nr = if (nr == 0x00 || nr == 0xAA) {
                            0
                        } else {
                            1
                        }
                        ng = if (ng == 0x00 || ng == 0xAA) {
                            0
                        } else {
                            1
                        }
                        nb = if (nb == 0x00 || nb == 0xAA) {
                            0
                        } else {
                            1
                        }
                        nr = 0x55 + nr * 0xAA
                        ng = 0x55 + ng * 0xAA
                        nb = 0x55 + nb * 0xAA
                    }
                }

                bitmap.pixels[i] = (nr shl 16) or (ng shl 8) or (nb shl 0)
            }
        }
    }
}