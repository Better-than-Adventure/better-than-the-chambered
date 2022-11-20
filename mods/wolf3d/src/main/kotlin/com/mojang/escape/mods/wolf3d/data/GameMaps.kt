package com.mojang.escape.mods.wolf3d.data

import java.io.DataInputStream
import java.io.InputStream

class GameMaps(bytes: ByteArray, mapHead: MapHead) {
    class LevelHeader(inputStream: InputStream) {
        val offPlane0: Int
        val offPlane1: Int
        val offPlane2: Int
        val lenPlane0: Int
        val lenPlane1: Int
        val lenPlane2: Int
        val width: Int
        val height: Int
        val name: String
        var plane0: Array<UShort>? = null
        var plane1: Array<UShort>? = null
        var plane2: Array<UShort>? = null
        
        init {
            val stream = DataInputStream(inputStream)
            offPlane0 = stream.readIntLE()
            offPlane1 = stream.readIntLE()
            offPlane2 = stream.readIntLE()
            lenPlane0 = stream.readUShortLE()
            lenPlane1 = stream.readUShortLE()
            lenPlane2 = stream.readUShortLE()
            width = stream.readUShortLE()
            height = stream.readUShortLE()
            name = stream.readString()
        }
    }
    
    val levelHeaders = mutableListOf<LevelHeader>()
    
    init {
        for (pointer in mapHead.pointers) {
            if (pointer < 1) continue
            val levelHeader = bytes.inputStream().use {
                it.skip(pointer.toLong())
                val levelHeader = LevelHeader(it)
                levelHeaders.add(levelHeader)
                return@use levelHeader
            }
            if (levelHeader.offPlane0 > 0) {
                levelHeader.plane0 = bytes.inputStream().use {
                    it.skip(levelHeader.offPlane0.toLong())
                    val plane0Bytes = Decompressor.decompress(it, levelHeader.lenPlane0, Decompressor.CompressionType.RLEW_CARMACK)
                    val ushortArray = Array(plane0Bytes.size / 2) { 0.toUShort() }
                    for (i in ushortArray.indices) {
                        ushortArray[i] = ((plane0Bytes[(i * 2) + 1].toUByte().toInt() shl 8) or (plane0Bytes[i * 2].toUByte().toInt() shl 0)).toUShort()
                    }
                    return@use ushortArray
                }
            }
            if (levelHeader.offPlane1 > 0) {
                levelHeader.plane1 = bytes.inputStream().use {
                    it.skip(levelHeader.offPlane1.toLong())
                    val plane1Bytes = Decompressor.decompress(it, levelHeader.lenPlane1, Decompressor.CompressionType.RLEW_CARMACK)
                    val ushortArray = Array(plane1Bytes.size / 2) { 0.toUShort() }
                    for (i in ushortArray.indices) {
                        ushortArray[i] = ((plane1Bytes[(i * 2) + 1].toUByte().toInt() shl 8) or (plane1Bytes[i * 2].toUByte().toInt() shl 0)).toUShort()
                    }
                    return@use ushortArray
                }
            }
            if (levelHeader.offPlane2 > 0) {
                levelHeader.plane2 = bytes.inputStream().use {
                    it.skip(levelHeader.offPlane2.toLong())
                    val plane2Bytes = Decompressor.decompress(it, levelHeader.lenPlane2, Decompressor.CompressionType.RLEW_CARMACK)
                    val ushortArray = Array(plane2Bytes.size / 2) { 0.toUShort() }
                    for (i in ushortArray.indices) {
                        ushortArray[i] = ((plane2Bytes[(i * 2) + 1].toUByte().toInt() shl 8) or (plane2Bytes[i * 2].toUByte().toInt() shl 0)).toUShort()
                    }
                    return@use ushortArray
                }
            }
        }
    }
}