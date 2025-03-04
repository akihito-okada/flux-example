package com.example.flux.model.util

import android.content.Context
import android.content.res.Resources

class ResourceProvider private constructor(private val context: Context) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }

    fun getStringArray(resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    val resources: Resources
        get() = context.resources

    companion object {
        fun getInstance(context: Context): ResourceProvider {
            return ResourceProvider(context)
        }
    }
}
