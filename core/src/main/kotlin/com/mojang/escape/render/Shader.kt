package com.mojang.escape.render

import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import java.io.Closeable
import java.io.FileNotFoundException

class Shader(name: String, vararg others: String): Closeable {
    private var shaderProgram: Int = -1
    private val uniforms = mutableMapOf<String, Int>()
    
    init {
        // Read in shader text
        val vertText = Shader::class.java.getResource("/shader/$name.vert")?.readText()
        val fragText = Shader::class.java.getResource("/shader/$name.frag")?.readText()
        if (vertText == null || fragText == null) {
            throw FileNotFoundException("/shader/$name")
        }
        val othersText = others.map {
            return@map Shader::class.java.getResource("/shader/$it.glsl")?.readText()
                ?: throw FileNotFoundException("/shader/$it.glsl")
        }
        
        // Create shaders 
        val vertexShader = glCreateShader(GL_VERTEX_SHADER)
        val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        val otherShaders = others.map {
            glCreateShader(GL_FRAGMENT_SHADER)
        }
        
        // Bind text to shaders and compile
        glShaderSource(vertexShader, vertText)
        glShaderSource(fragmentShader, fragText)
        otherShaders.withIndex().map {
            glShaderSource(it.value, othersText[it.index])
        }
        glCompileShader(vertexShader)
        glCompileShader(fragmentShader)
        otherShaders.map { 
            glCompileShader(it)
        }
        
        // Check for success
        MemoryStack.stackPush().use { stack -> 
            val buffer = stack.mallocInt(1)
            
            glGetShaderiv(vertexShader, GL_COMPILE_STATUS, buffer)
            if (buffer[0] == 0) {
                val err = glGetShaderInfoLog(vertexShader)
                throw ShaderCompilationException(err, ShaderCompilationException.CompilationStage.COMPILE_VERTEX_SHADER)
            }
            
            glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, buffer)
            if (buffer[0] == 0) {
                val err = glGetShaderInfoLog(fragmentShader)
                throw ShaderCompilationException(err, ShaderCompilationException.CompilationStage.COMPILE_FRAGMENT_SHADER)
            }
            
            otherShaders.map {
                glGetShaderiv(it, GL_COMPILE_STATUS, buffer)
                if (buffer[0] == 0) {
                    val err = glGetShaderInfoLog(it)
                    throw ShaderCompilationException(err, ShaderCompilationException.CompilationStage.COMPILE_OTHER_SHADER)
                }
            }
        }
        
        // Create shader program
        shaderProgram = glCreateProgram()
        
        // Attach shaders and link
        glAttachShader(shaderProgram, vertexShader)
        glAttachShader(shaderProgram, fragmentShader)
        otherShaders.map {
            glAttachShader(shaderProgram, it)
        }
        glLinkProgram(shaderProgram)
        
        // Check for success
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocInt(1)
            
            glGetProgramiv(shaderProgram, GL_LINK_STATUS, buffer)
            if (buffer[0] == 0) {
                val err = glGetProgramInfoLog(shaderProgram)
                throw ShaderCompilationException(err, ShaderCompilationException.CompilationStage.CREATE_SHADER_PROGRAM)
            }
        }
        
        // Delete shaders (no longer needed)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        otherShaders.map {
            glDeleteShader(it)
        }
    }
    
    fun bind() {
        glUseProgram(shaderProgram)
    }
    
    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(shaderProgram, uniformName)
        if (uniformLocation < 0) {
            throw Exception("Could not find uniform $uniformName")
        }
        uniforms += uniformName to uniformLocation
    }
    
    fun setUniform(uniformName: String, value: Int) {
        glUniform1i(uniforms[uniformName]!!, value)
    }

    override fun close() {
        glDeleteProgram(shaderProgram)
    }
}

class ShaderCompilationException(val error: String, val stage: CompilationStage): Exception() {
    enum class CompilationStage {
        COMPILE_VERTEX_SHADER,
        COMPILE_FRAGMENT_SHADER,
        COMPILE_OTHER_SHADER,
        CREATE_SHADER_PROGRAM
    }

    override fun toString(): String {
        return "Shader compilation failed at stage ${stage.name}: $error"
    }
}