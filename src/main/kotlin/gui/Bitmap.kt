package com.mojang.escape.gui

import com.mojang.escape.Game
import com.mojang.escape.lang.Language
import com.mojang.escape.util.TranslatedString

open class Bitmap(val width: Int, val height: Int) {
    val pixels = IntArray(width * height)

    fun draw(bitmap: Bitmap, xOffs: Int, yOffs: Int) {
        for (y in 0 until bitmap.height) {
            val yPix = y + yOffs
            if (yPix < 0 || yPix >= height) {
                continue
            }

            for (x in 0 until bitmap.width) {
                val xPix = x + xOffs
                if (xPix < 0 || xPix >= width) {
                    continue
                }

                val src = bitmap.pixels[x + y * bitmap.width]
                pixels[xPix + yPix * width] = src
            }
        }
    }

    fun drawFlipped(bitmap: Bitmap, xOffs: Int, yOffs: Int) {
        for (y in 0 until bitmap.height) {
            val yPix = y + yOffs
            if (yPix < 0 || yPix >= height) {
                continue
            }

            for (x in 0 until bitmap.width) {
                val xPix = x + xOffs - x - 1
                if (xPix < 0 || xPix >= width) {
                    continue
                }

                val src = bitmap.pixels[x + y * bitmap.width]
                pixels[xPix + yPix * width] = src
            }
        }
    }

    fun draw(bitmap: Bitmap, xOffs: Int, yOffs: Int, xo: Int, yo: Int, w: Int, h: Int, col: Int) {
        for (y in 0 until h) {
            val yPix = y + yOffs
            if (yPix < 0 || yPix >= height) {
                continue
            }

            for (x in 0 until w) {
                val xPix = x + xOffs
                if (xPix < 0 || xPix >= width) {
                    continue
                }
                val src = bitmap.pixels[(x + xo) + (y + yo) * bitmap.width]
                if (src >= 0) {
                    pixels[xPix + yPix * width] = src * col
                }
            }
        }
    }

    fun scaleDraw(bitmap: Bitmap, scale: Int, xOffs: Int, yOffs: Int, xo: Int, yo: Int, w: Int, h: Int, col: Int) {
        for (y in 0 until (h * scale)) {
            val yPix = y + yOffs
            if (yPix < 0 || yPix >= height) {
                continue
            }

            for (x in 0 until (w * scale)) {
                val xPix = x + xOffs
                if (xPix < 0 || xPix >= width) {
                    continue
                }

                val src = bitmap.pixels[(x / scale + xo) + (y / scale + yo) * bitmap.width]
                if (src >= 0) {
                    pixels[xPix + yPix * width] = src * col
                }
            }
        }
    }

    fun draw(string: TranslatedString, x: Int, y: Int, col: Int, lang: Language = Game.lang) {
        draw(string.value, x, y, col, lang)
    }

    fun draw(string: String, x: Int, y: Int, col: Int, lang: Language = Game.lang) {
        for (i in string.indices) {
            val ch = lang.fontString.indexOf(string[i])
            if (ch < 0) continue

            val xx = ch % 42
            val yy = ch / 42
            draw(lang.fontBitmap, x + i * 6, y, xx * 6, yy * 8, 5, 8, col)
        }
    }

    fun fill(x0: Int, y0: Int, x1: Int, y1: Int, color: Int) {
        for (y in y0 until y1) {
            for (x in x0 until x1) {
                pixels[x + y * width] = color
            }
        }
    }
}