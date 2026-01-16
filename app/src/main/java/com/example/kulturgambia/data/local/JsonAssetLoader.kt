package com.example.kulturgambia.data.local

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class JsonAssetLoader(private val context: Context) {

    val parser = Json { ignoreUnknownKeys = true }

    fun loadText(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

    inline fun <reified T> load(fileName: String): T {
        val text = loadText(fileName)
        return parser.decodeFromString(text)
    }
}
