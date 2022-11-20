package com.mojang.escape.level.wolf3d

import java.io.DataInputStream

fun DataInputStream.readIntLE(): Int {
    val value = readInt()
    return (((value shr 0) and 0xFF) shl 24) or (((value shr 8) and 0xFF) shl 16) or (((value shr 16) and 0xFF) shl 8) or (((value shr 24) and 0xFF) shl 0)
}

fun DataInputStream.readUShortLE(): Int {
    val value = readShort().toInt()
    return (((value shr 0) and 0xFF) shl 8) or (((value shr 8) and 0xFF) shl 0)
}

fun DataInputStream.readString(): String {
    var value = ""
    var lastChar = readUnsignedByte()
    while (lastChar != 0) {
        value += Char(lastChar)
        lastChar = readUnsignedByte()
    }
    return value
}