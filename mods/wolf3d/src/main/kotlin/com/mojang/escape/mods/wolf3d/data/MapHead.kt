package com.mojang.escape.mods.wolf3d.data

import java.io.DataInputStream
import java.io.InputStream
import java.lang.Exception

class MapHead(bytes: ByteArray) {
    val magic: Int
    val pointers = IntArray(100)
    
    init {
        DataInputStream(bytes.inputStream()).use { stream ->
            magic = stream.readUShortLE()
            if (magic != 0xABCD) {
                throw Exception("MAPHEAD magic word was wrong! (expected 0xABCD, got ${String.format("0x%04X", magic)})")
            }
            for (i in 0 until 100) {
                pointers[i] = stream.readIntLE()
            }
        }
    }
}