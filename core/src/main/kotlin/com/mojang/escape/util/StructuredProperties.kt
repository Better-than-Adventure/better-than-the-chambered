package com.mojang.escape.util

import com.mojang.escape.indent
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.util.Properties

class StructuredProperties: Properties() {
    override fun load(inStream: InputStream) {
        load0(BufferedReader(InputStreamReader(inStream, "UTF-8")))
    }

    override fun load(reader: Reader) {
        load0(BufferedReader(reader))
    }

    // TODO: Rewrite this to be safer and smarter
    private fun load0(reader: BufferedReader) {
        val parts = mutableListOf<String>()
        for (line in reader.lines()) {
            val indent = line.indent / 4
            val line = line.trimStart()
            while (parts.size > indent) {
                parts.removeLast()
            }
            if (!line.contains('=')) {
                parts += line.trim('.')
            } else {
                val split = line.split("=")
                setProperty(parts.joinToString(".") + "." + split[0], split[1])
            }
        }
    }
}