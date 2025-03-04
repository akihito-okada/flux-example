package com.example.flux.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer

object GsonFactory {

    fun create(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(
                Boolean::class.javaPrimitiveType,
                JsonDeserializer { json, _, _ -> json.toString() == "true" },
            )
            .serializeNulls()
            .create()
    }
}
