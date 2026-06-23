package com.maskmasteruk.supabaseandroid.supabase

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.serialization.json.JsonObject

object PostgresManager {

    private fun client() = AuthManager.getClient()

    /**
     * Select rows from a table with optional filters and ordering.
     * @return List of [JsonObject] rows, or empty list if none found.
     */
    suspend fun select(
        tableName: String,
        filters: (PostgrestFilterBuilder.() -> Unit)? = null,
        orderBy: String? = null,
        ascending: Boolean = true
    ): List<JsonObject> {
        return runCatching {
            client().from(tableName).select {
                filter {
                    filters?.invoke(this)
                }
                if (orderBy != null) {
                    order(
                        column = orderBy,
                        order = if (ascending) Order.ASCENDING else Order.DESCENDING
                    )
                }
            }.decodeList<JsonObject>()
        }.getOrElse { e ->
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Insert a row into a table.
     * @return The inserted row as [JsonObject], or null on failure.
     */
    suspend fun insert(
        tableName: String,
        data: JsonObject
    ): JsonObject? {
        return runCatching {
            client().from(tableName).insert(data) {
                select()
            }.decodeSingle<JsonObject>()
        }.getOrElse { e ->
            e.printStackTrace()
            null
        }
    }

    /**
     * Update rows matching the given filters.
     * @return List of updated rows as [JsonObject], or empty list on failure.
     */
    suspend fun update(
        tableName: String,
        data: JsonObject,
        filters: (PostgrestFilterBuilder.() -> Unit)? = null
    ): List<JsonObject> {
        return runCatching {
            client().from(tableName).update(data) {
                select()
                filter {
                    filters?.invoke(this)
                }
            }.decodeList<JsonObject>()
        }.getOrElse { e ->
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Delete rows matching the given filters.
     * @return True if deletion succeeded, false otherwise.
     */
    suspend fun delete(
        tableName: String,
        filters: (PostgrestFilterBuilder.() -> Unit)? = null
    ): Boolean {
        return runCatching {
            client().from(tableName).delete {
                filter {
                    filters?.invoke(this)
                }
            }
            true
        }.getOrElse { e ->
            e.printStackTrace()
            false
        }
    }

    suspend fun callRPC(
        functionName: String,
        params: JsonObject
    ): PostgrestResult {
        return client().postgrest.rpc(
            function = functionName,
            parameters = params
        )
    }
}