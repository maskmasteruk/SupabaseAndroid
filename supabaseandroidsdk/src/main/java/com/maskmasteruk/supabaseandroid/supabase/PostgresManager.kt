package com.maskmasteruk.supabaseandroid.supabase

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.serialization.json.JsonObject

/**
 * Singleton manager for handling Supabase Postgres (PostgREST) operations.
 */
object PostgresManager {

    private fun client() = AuthManager.getClient()

    /**
     * Select rows from a table with optional filters and ordering.
     * @param tableName The table to query.
     * @param filters Optional filter builder block.
     * @param orderBy Optional column name to order results by.
     * @param ascending Whether to order ascending (true) or descending (false).
     * @return List of [JsonObject] rows, or empty list if none found or error occurs.
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
     * @param tableName The table to insert into.
     * @param data The row data as [JsonObject].
     * @return The inserted row as [JsonObject] (including database-generated columns), or null on failure.
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
     * @param tableName The table to update.
     * @param data The new values for the columns.
     * @param filters Filter builder block to identify rows to update.
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
     * @param tableName The table to delete from.
     * @param filters Filter builder block to identify rows to delete.
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

    /**
     * Calls a Supabase RPC (stored procedure).
     * @param functionName The name of the database function.
     * @param params Parameters for the function as [JsonObject].
     * @return [PostgrestResult] containing the response.
     */
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