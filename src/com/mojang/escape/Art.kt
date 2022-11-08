package com.mojang.escape

import com.mojang.escape.gui.Bitmap
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.lang.RuntimeException
import java.net.URL
import javax.imageio.ImageIO

object Art {
    val walls = loadBitmap("/tex/walls.png")
    val floors = loadBitmap("/tex/floors.png")
    val sprites = loadBitmap("/tex/sprites.png")
    val font = loadBitmap("/tex/font.png")
    val panel = loadBitmap("/tex/gamepanel.png")
    val items = loadBitmap("/tex/items.png")
    val sky = loadBitmap("/tex/sky.png")

    val logo = loadBitmap("/gui/logo.png")

    /**
     * Returns [c], formatted so that it can be used as a modifier colour for a drawn texture.
     */
    fun getCol(c: Int): Int {
        var r: Int = (c shr 16) and 0xFF
        var g: Int = (c shr 8)  and 0xFF
        var b: Int = (c shr 0)  and 0xFF

        r = r * 0x55 / 0xFF
        g = g * 0x55 / 0xFF
        b = b * 0x55 / 0xFF

        return (r shl 16) or (g shl 8) or (b shl 0)
    }

    /**
     * Returns a bitmap loaded from the given [fileName].
     * @throws RuntimeException Thrown when the given image could not be read.
     * @throws FileNotFoundException Thrown when the given path could not be found.
     */
    private fun loadBitmap(fileName: String): Bitmap {
        val fileUrl: URL? = Art::class.java.getResource(fileName)
        if (fileUrl != null) {
            try {
                val img: BufferedImage = ImageIO.read(fileUrl)

                val result = Bitmap(img.width, img.height)
                img.getRGB(0, 0, img.width, img.height, result.pixels, 0, img.width)
                result.pixels.forEachIndexed { index, pixel ->
                    var col = (pixel and 0xf) shr 2;
                    if (pixel.toUInt() == 0xFFFF00FFu) col = -1;
                    result.pixels[index] = col;
                }
                return result
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        throw FileNotFoundException(fileName)
    }


}