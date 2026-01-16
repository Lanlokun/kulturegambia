package com.example.kulturgambia.data.util

import android.content.Context

fun drawableIdByName(context: Context, name: String): Int? {
    if (name.isBlank()) return null
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    return if (id == 0) null else id
}
