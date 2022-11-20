package com.mojang.escape.render

import com.mojang.escape.Escape
import com.mojang.escape.gui.Bitmap
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL42.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Display: Renderable {
    private val vertices = arrayOf(
        // Top left triangle
        -1f,  1f,  0f, // Top left
         1f,  1f,  0f, // Top right
        -1f, -1f,  0f, // Bottom left
        // Bottom right triangle
         1f,  1f,  0f, // Top right
         1f, -1f,  0f, // Bottom right
        -1f, -1f,  0f, // Bottom left
    ).toFloatArray()
    private val texCoords = arrayOf(
        // Top left triangle
        0f, 1f,
        1f, 1f,
        0f, 0f,
        // Bottom right triangle
        1f, 1f,
        1f, 0f,
        0f, 0f
    ).toFloatArray()
    private var vbos = mutableListOf<Int>()
    private var vao: Int = -1
    private var texture: Int = -1
    
    private lateinit var shader: Shader
    
    var bitmap: Bitmap? = null
    val bitmapBuffer = ByteBuffer.allocateDirect(Escape.WIDTH * Escape.HEIGHT * 4).order(ByteOrder.nativeOrder())
    
    override fun init() {
        // Get shader
        shader = Shader("display")
        shader.createUniform("tex")
        
        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        // Set up vertices VBO
        var vbo = glGenBuffers()
        vbos += vbo
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        // Set up texture coords VBO
        vbo = glGenBuffers()
        vbos += vbo
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * 4, 0)
        
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)        
        
        // Set up texture
        texture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texture)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Escape.WIDTH, Escape.HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
    }

    override fun render() {
        shader.bind()
        shader.setUniform("tex", 0)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture)
        glEnable(GL_TEXTURE_2D)
        if (bitmap != null) {
            bitmapBuffer.put(bitmap!!.separatedPixels)
            bitmapBuffer.flip()
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, Escape.WIDTH, Escape.HEIGHT, GL_RGBA, GL_UNSIGNED_BYTE, bitmapBuffer)
        }
        
        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawArrays(GL_TRIANGLES, 0, 6)
        
        glDisable(GL_TEXTURE_2D)
    }

}