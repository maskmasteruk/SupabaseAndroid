package com.maskmasteruk.supabaseandroid.supabase

import com.maskmasteruk.supabaseandroid.objects.Error
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

/**
 * A bridge between the asynchronous Supabase Postgres (PostgREST) operations and the UI layer.
 * Provides callbacks for Java/Android interoperability.
 */
object PostgresBridge {

    /**
     * Callback interface for Select and Update operations returning multiple rows.
     */
    interface CrudCallback {
        /**
         * Called when the operation succeeds.
         * @param result List of [JsonObject] representing the rows.
         */
        fun onSuccess(result: List<JsonObject>)

        /**
         * Called when the operation fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Callback interface for Insert operations returning a single row.
     */
    interface InsertCallback {
        /**
         * Called when the insert operation succeeds.
         * @param result The inserted row as a [JsonObject], or null if nothing returned.
         */
        fun onSuccess(result: JsonObject?)

        /**
         * Called when the operation fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Callback interface for Update operations returning multiple rows.
     */
    interface UpdateCallback {
        /**
         * Called when the update operation succeeds.
         * @param result List of updated [JsonObject] rows.
         */
        fun onSuccess(result: List<JsonObject>)

        /**
         * Called when the operation fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Callback interface for Delete operations.
     */
    interface DeleteCallback {
        /**
         * Called when the delete operation completes.
         * @param deleted True if the operation was successful.
         */
        fun onSuccess(deleted: Boolean)

        /**
         * Called when the operation fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Callback interface for RPC (stored procedure) calls.
     */
    interface RPCCallback {
        /**
         * Called when the RPC call succeeds.
         * @param result The [PostgrestResult] containing response data.
         */
        fun onSuccess(result: PostgrestResult)

        /**
         * Called when the RPC call fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Selects rows from a table.
     * @param tableName The table name.
     * @param filters Optional filter builder block.
     * @param orderBy Optional column name to order by.
     * @param ascending Whether the order should be ascending.
     * @param callback Callback for success or error.
     */
    @JvmStatic
    fun select(
        tableName: String,
        filters: (PostgrestFilterBuilder.() -> Unit)? = null,
        orderBy: String? = null,
        ascending: Boolean = true,
        callback: CrudCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = PostgresManager.select(tableName, filters, orderBy, ascending)
                withContext(Dispatchers.Main) { callback.onSuccess(result) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback.onError(Error(e)) }
            }
        }
    }

    /**
     * Inserts a new row into a table.
     * @param tableName The table name.
     * @param data The row data as a [JsonObject].
     * @param callback Callback for success or error.
     */
    @JvmStatic
    fun insert(
        tableName: String,
        data: JsonObject,
        callback: InsertCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = PostgresManager.insert(tableName, data)
                withContext(Dispatchers.Main) { callback.onSuccess(result) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback.onError(Error(e)) }
            }
        }
    }

    /**
     * Updates rows in a table.
     * @param tableName The table name.
     * @param data The new data for the rows.
     * @param filters Filter builder to identify rows to update.
     * @param callback Callback for success or error.
     */
    @JvmStatic
    fun update(
        tableName: String,
        data: JsonObject,
        filters: (PostgrestFilterBuilder.() -> Unit)? = null,
        callback: UpdateCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = PostgresManager.update(tableName, data, filters)
                withContext(Dispatchers.Main) { callback.onSuccess(result) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback.onError(Error(e)) }
            }
        }
    }

    /**
     * Deletes rows from a table.
     * @param tableName The table name.
     * @param filters Filter builder to identify rows to delete.
     * @param callback Callback for success or error.
     */
    @JvmStatic
    fun delete(
        tableName: String,
        filters: (PostgrestFilterBuilder.() -> Unit)? = null,
        callback: DeleteCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = PostgresManager.delete(tableName, filters)
                withContext(Dispatchers.Main) { callback.onSuccess(result) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback.onError(Error(e)) }
            }
        }
    }

    /**
     * Calls a Supabase RPC (stored procedure).
     * @param functionName The name of the database function.
     * @param params Parameters for the function as a [JsonObject].
     * @param callback Callback for success or error.
     */
    @JvmStatic
    fun callRPC(
        functionName: String,
        params: JsonObject,
        callback: RPCCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = PostgresManager.callRPC(functionName, params)
                withContext(Dispatchers.Main) { callback.onSuccess(result) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback.onError(Error(e)) }
            }
        }
    }

    /**
     * Calls a Supabase RPC (stored procedure) without parameters.
     * @param functionName The name of the database function.
     * @param callback Callback for success or error.
     */
    @JvmStatic
    fun callRPC(
        functionName: String,
        callback: RPCCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = PostgresManager.callRPC(functionName, buildJsonObject {  })
                withContext(Dispatchers.Main) { callback.onSuccess(result) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback.onError(Error(e)) }
            }
        }
    }
}