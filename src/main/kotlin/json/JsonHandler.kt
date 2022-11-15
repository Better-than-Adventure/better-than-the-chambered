package com.mojang.escape.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonHandler {
    companion object {
        fun readFile(filePath: String)
                = this::class.java.getResource(filePath)?.readText(Charsets.UTF_8)

        inline fun <reified T> T.toJson(): String = Json.encodeToString(this)

        inline fun <reified T> String.fromJson(): T = Json.decodeFromString(this)
    }



}