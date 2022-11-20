package com.mojang.escape.gui.palette

abstract class Palette {
    companion object {
        val ega = EGAPalette()
        val cga = CGAPalette()
    }

    protected abstract val values: Array<Triple<Int, Int, Int>>

    fun iterator(): Iterator<Triple<Int, Int, Int>> {
        return values.iterator()
    }
}