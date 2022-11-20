package com.mojang.escape.gui

import com.mojang.escape.lang.StringUnit

open class Bitmap(val width: Int, val height: Int) {
    val pixels = IntArray(width * height)
    
    val separatedPixels: ByteArray
        get() {
            val arr = ByteArray(width * height * 4)
            for (x in 0 until width) {
                for (y in (height - 1) downTo 0) {
                    val i = x + (y * width)
                    val revI = x + (((height - 1) - y) * width)
                    arr[(revI * 4) + 0] = ((pixels[i] shr 16) and 0xFF).toByte()
                    arr[(revI * 4) + 1] = ((pixels[i] shr 8 ) and 0xFF).toByte()
                    arr[(revI * 4) + 2] = ((pixels[i] shr 0 ) and 0xFF).toByte()
                    arr[(revI * 4) + 3] = 0xFF.toByte()
                }
            }
            return arr
        }

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

    fun draw(translatable: StringUnit, x: Int, y: Int, col: Int) {
        translatable.draw(this, x, y, col)
    }

    fun fill(x0: Int, y0: Int, x1: Int, y1: Int, color: Int) {
        for (y in y0 until y1) {
            for (x in x0 until x1) {
                pixels[x + y * width] = color
            }
        }
    }
}