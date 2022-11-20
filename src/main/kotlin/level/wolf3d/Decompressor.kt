package com.mojang.escape.level.wolf3d

import com.mojang.escape.equalsTo
import java.io.DataInputStream
import java.io.InputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)
object Decompressor {
    enum class CompressionType {
        RLEW,
        RLEW_CARMACK,
        RLEW_HUFFMAN
    }

    fun decompress(inputStream: InputStream, length: Int, type: CompressionType): ByteArray {
        var inputArray = DataInputStream(inputStream).use { stream ->
            // First read [length] bytes
            val inputArray = ByteArray(length)
            stream.read(inputArray)

            return@use inputArray
        }.toUByteArray()
        val decompressed = when (type) {
            CompressionType.RLEW -> inputArray
            CompressionType.RLEW_CARMACK -> decarmackize(inputArray)
            CompressionType.RLEW_HUFFMAN -> dehuffmanize(inputArray)
        }
        val derlewed = derlew(decompressed)

        return derlewed.toByteArray()
    }
    
    private fun derlew(compressedData: UByteArray): UByteArray {
        val output = mutableListOf<UByte>()
        var wordI = 2
        val nWordsMax = compressedData.size / 2
        
        while (wordI < nWordsMax) {
            val offset = wordI * 2
            val wordBytes = compressedData.slice(offset until (offset + 2)).toUByteArray()
            if (wordBytes equalsTo arrayOf(0xCD.toUByte(), 0xAB.toUByte()).toUByteArray()) {
                if (wordI + 1 == nWordsMax) {
                    break
                }
                val count = (compressedData[offset + 3].toUShort().toInt() shl 8) or (compressedData[offset + 2].toUShort().toInt())
                for (i in 0 until count) {
                    output += compressedData[offset + 4]
                    output += compressedData[offset + 5]
                }
                wordI += 3
            } else {
                output.addAll(compressedData.slice(offset until (offset + 2)))
                wordI += 1
            }
        }
        return output.toUByteArray()
    }
    
    private fun decarmackize(compressedData: UByteArray): UByteArray {
        val NEARPOINTER = 0xA7.toUByte()
        val FARPOINTER = 0xA8.toUByte()
        val output = mutableListOf<UByte>()
        var wordI = 0
        var nShifts = 0
        var offset = 0
        
        while (offset < compressedData.size - 2) {
            val slice = compressedData.slice(offset until (offset + 2)).toUByteArray()
            if (slice equalsTo arrayOf(0x00.toUByte(), NEARPOINTER).toUByteArray() || slice equalsTo arrayOf(0x00.toUByte(), FARPOINTER).toUByteArray()) {
                output.add(compressedData[offset + 2])
                output.add(compressedData[offset + 1])
                nShifts += 1
            } else if (slice[1] == NEARPOINTER) {
                val count = slice[0]
                val distance = compressedData[offset + 2].toInt()
                val segmentStart = output.size - distance * 2
                val segmentEnd = segmentStart + count.toInt() * 2
                val segmentToRepeat = output.slice(segmentStart until segmentEnd)
                output.addAll(segmentToRepeat)
                nShifts += 1
            } else if (slice[1] == FARPOINTER) {
                val count = slice[0]
                val distance = (compressedData[offset + 3].toUShort().toInt() shl 8) or (compressedData[offset + 2].toUShort().toInt())
                val segmentStart = (distance + 1) * 2
                val segmentEnd = segmentStart + count.toInt() * 2
                val segmentToRepeat = output.slice(segmentStart until segmentEnd)
                output.addAll(segmentToRepeat)
                wordI++
            } else {
                output.addAll(slice.toList())
            }
            
            wordI += 1
            offset = wordI * 2 + nShifts
        }
        
        if (offset < compressedData.size) {
            val remainder = compressedData.slice(offset until compressedData.size)
            output.addAll(remainder)
        }
        
        return output.toUByteArray()
    }
    
    private fun dehuffmanize(compressedData: UByteArray): UByteArray {
        return compressedData
    }
}