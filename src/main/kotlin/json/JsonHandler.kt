package com.mojang.escape.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.lang.RuntimeException

object JsonHandler {
    inline fun <reified T> populateJsonObject(filePath: String): T {
        val fileResource = JsonHandler::class.java.getResourceAsStream(filePath)
        if(fileResource != null){
            try {
                val json = fileResource.bufferedReader().use { it.readText() }
                return Json.decodeFromString(json);

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        throw FileNotFoundException(filePath)
    }

    inline fun <reified T> T.toJson(): String = Json.encodeToString(this)

    inline fun <reified T> String.fromJson(): T = Json.decodeFromString(this)




}