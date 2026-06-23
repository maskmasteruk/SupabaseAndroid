package com.maskmasteruk.supabaseandroid.utils

import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Utility object for handling JSON operations and conversions between Kotlin/Java types and Supabase JSON types.
 */
object JsonUtils {

    /**
     * Builder class for creating [JsonObject] instances in a fluent way.
     */
    class JsonParamBuilder {

        private val params = mutableMapOf<String, Any?>()

        /**
         * Adds a key-value pair to the JSON object.
         * @param key The JSON key.
         * @param value The value, which can be String, Number, Boolean, or null. Other types will be converted to String.
         * @return The builder instance.
         */
        fun add(key: String, value: Any?): JsonParamBuilder {
            params[key] = value
            return this
        }

        /**
         * Builds and returns the [JsonObject].
         */
        fun build(): JsonObject {
            return buildJsonObject {
                params.forEach { (key, value) ->
                    when (value) {
                        null -> put(key, JsonNull)
                        is String -> put(key, JsonPrimitive(value))
                        is Number -> put(key, JsonPrimitive(value))
                        is Boolean -> put(key, JsonPrimitive(value))
                        else -> put(key, JsonPrimitive(value.toString()))
                    }
                }
            }
        }
    }

    /**
     * Converts a Map to a [JsonObject].
     */
    @JvmStatic
    fun mapToJsonObject(map: Map<String, Any?>): JsonObject {
        return buildJsonObject {
            map.forEach { (key, value) ->
                when (value) {
                    null -> put(key, JsonNull)
                    is String -> put(key, JsonPrimitive(value))
                    is Number -> put(key, JsonPrimitive(value))
                    is Boolean -> put(key, JsonPrimitive(value))
                    else -> put(key, JsonPrimitive(value.toString()))
                }
            }
        }
    }

    /**
     * Creates a [JsonObject] from a variable number of arguments (key-value pairs).
     * @param args Pairs of (String, Any?).
     * @throws IllegalArgumentException if the number of arguments is not even or if a key is not a String.
     */
    @JvmStatic
    fun jsonObjectOf(vararg args: Any?): JsonObject {
        require(args.size % 2 == 0) {
            "Arguments must be key-value pairs"
        }

        return buildJsonObject {
            for (i in args.indices step 2) {
                val key = args[i] as? String
                    ?: throw IllegalArgumentException("Key at index $i must be a String")

                val value = args[i + 1]

                when (value) {
                    null -> put(key, JsonNull)
                    is String -> put(key, JsonPrimitive(value))
                    is Number -> put(key, JsonPrimitive(value))
                    is Boolean -> put(key, JsonPrimitive(value))
                    else -> put(key, JsonPrimitive(value.toString()))
                }
            }
        }
    }

    /**
     * Creates a [JsonObject] from Map entries.
     */
    @JvmStatic
    fun jsonObjectOf(vararg entries: Map.Entry<String, *>): JsonObject {
        return buildJsonObject {
            entries.forEach { entry ->
                val key = entry.key
                val value = entry.value

                when (value) {
                    null -> put(key, JsonNull)
                    is String -> put(key, JsonPrimitive(value))
                    is Number -> put(key, JsonPrimitive(value))
                    is Boolean -> put(key, JsonPrimitive(value))
                    else -> put(key, JsonPrimitive(value.toString()))
                }
            }
        }
    }


    /**
     * Extracts a [JsonObject] from a [PostgrestResult].
     */
    @JvmStatic
    fun getJsonObject(result: PostgrestResult): JsonObject {
        return Json.parseToJsonElement(result.data).jsonObject
    }

    /**
     * Extracts a String from a [PostgrestResult].
     */
    @JvmStatic
    fun getString(result: PostgrestResult): String {
        return Json.parseToJsonElement(result.data).jsonPrimitive.content
    }


    /**
     * Extracts a Boolean from a [PostgrestResult].
     */
    @JvmStatic
    fun getBoolean(result: PostgrestResult): Boolean {
        return Json
            .parseToJsonElement(result.data)
            .jsonPrimitive
            .boolean
    }

    /**
     * Extracts a [JsonArray] from a [PostgrestResult].
     */
    @JvmStatic
    fun getJsonArray(result: PostgrestResult): JsonArray {
        return Json
            .parseToJsonElement(result.data)
            .jsonArray
    }

}