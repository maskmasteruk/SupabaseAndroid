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

object PostgresBridge {

    interface CrudCallback {
        fun onSuccess(result: List<JsonObject>)
        fun onError(error: Error)
    }

    interface InsertCallback {
        fun onSuccess(result: JsonObject?)
        fun onError(error: Error)
    }

    interface UpdateCallback {
        fun onSuccess(result: List<JsonObject>)
        fun onError(error: Error)
    }

    interface DeleteCallback {
        fun onSuccess(deleted: Boolean)
        fun onError(error: Error)
    }

    interface RPCCallback {
        fun onSuccess(result: PostgrestResult)
        fun onError(error: Error)
    }

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