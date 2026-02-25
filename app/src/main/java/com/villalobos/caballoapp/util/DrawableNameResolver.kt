package com.villalobos.caballoapp.util

import com.villalobos.caballoapp.R

object DrawableNameResolver {

    private val drawableByName: Map<String, Int> by lazy {
        buildMap {
            R.drawable::class.java.fields.forEach { field ->
                if (field.type == Int::class.javaPrimitiveType) {
                    put(field.name, field.getInt(null))
                }
            }
        }
    }

    fun resolve(name: String?): Int {
        if (name.isNullOrBlank()) return 0
        return drawableByName[name] ?: 0
    }
}
