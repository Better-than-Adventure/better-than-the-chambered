package com.mojang.escape.util

import com.mojang.escape.mods.IMod
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.awt.image.BufferedImage
import java.net.URL
import java.nio.ByteBuffer

class Image(val width: Int, val height: Int) {
    companion object {
        fun read(path: String, mod: IMod?): Image {
            val resource = if (mod != null) {
                mod::class.java.getResource(path)
            } else {
                Image::class.java.getResource(path)
            } ?: throw Exception("Could not open image ${path}!")
            val array = resource.readBytes()
            var width: Int
            var height: Int
            val buffer = ByteBuffer.allocateDirect(array.size)
            buffer.put(array)
            buffer.flip()
            val img = MemoryStack.stackPush().use { stack ->
                val _width = stack.mallocInt(1)
                val _height = stack.mallocInt(1)
                val channels = stack.mallocInt(1)
                
                val img = STBImage.stbi_load_from_memory(buffer, _width, _height, channels, 4)
                width = _width[0]
                height = _height[0]
                if (img == null) throw Exception("Could not open image ${path}!")
                return@use img
            }
            val bImg = Image(width, height)
            img.get(bImg.buffer)
            return bImg
        }
    }

    private var buffer = ByteArray(width * height * 4)

    fun getRGB(rgbArray: IntArray) {
        for (i in 0 until (width * height)) {
            rgbArray[i] =
                (buffer[(i * 4) + 0].toUByte().toInt() shl 16) or
                (buffer[(i * 4) + 1].toUByte().toInt() shl 8 ) or
                (buffer[(i * 4) + 2].toUByte().toInt() shl 0 ) or
                (buffer[(i * 4) + 3].toUByte().toInt() shl 24)
        }
    }
}