package com.maskmasteruk.supabaseandroidsdk.supabase

import android.content.Context
import android.net.Uri
import com.maskmasteruk.supabaseandroidsdk.objects.Error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

object StorageBridge {
    interface UploadCallback {
        fun onSuccess(url: String)
        fun onError(error: Error)
    }

    interface DeleteCallback {
        fun onSuccess(boolean: Boolean)
        fun onError(error: Error)
    }

    @JvmStatic
    fun uploadFile(
        bucket: String,
        path: String,
        file: File,
        callback: UploadCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = StorageManager.upload(
                    bucket, path, file
                )

                withContext(Dispatchers.Main) {
                    callback.onSuccess(url)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(
                        Error(e)
                    )
                }
            }
        }
    }

    @JvmStatic
    fun uploadFile(
        context: Context,
        bucket: String,
        path: String,
        uri: Uri,
        callback: UploadCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = StorageManager.upload(
                    context, bucket, path, uri
                )

                withContext(Dispatchers.Main) {
                    callback.onSuccess(url)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(
                        Error(e)
                    )
                }
            }
        }
    }

    @JvmStatic
    fun uploadBytes(
        bucket: String,
        path: String,
        bytes: ByteArray,
        mimeType: String,
        callback: UploadCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = StorageManager.upload(
                    bucket, path, bytes, mimeType
                )

                withContext(Dispatchers.Main) {
                    callback.onSuccess(url)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(
                        Error(e)
                    )
                }
            }
        }
    }

    @JvmStatic
    fun deleteFile(
        bucket: String,
        path: String,
        callback: DeleteCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                StorageManager.delete(bucket, path)

                withContext(Dispatchers.Main) {
                    callback.onSuccess(true)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(
                        Error(e)
                    )
                }
            }
        }
    }

    @JvmStatic
    fun deleteFile(
        bucket: String,
        paths: List<String>,
        callback: DeleteCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                StorageManager.delete(bucket, paths)

                withContext(Dispatchers.Main) {
                    callback.onSuccess(true)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(
                        Error(e)
                    )
                }
            }
        }
    }
}