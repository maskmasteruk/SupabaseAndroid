package com.maskmasteruk.supabaseandroid.supabase

import android.content.Context
import android.net.Uri
import com.maskmasteruk.supabaseandroid.objects.Error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * A bridge between the asynchronous Supabase Storage operations and the UI layer.
 * Provides callbacks for Java/Android interoperability.
 */
object StorageBridge {
    /**
     * Callback interface for file upload operations.
     */
    interface UploadCallback {
        /**
         * Called when the file is successfully uploaded.
         * @param url The public URL of the uploaded file.
         */
        fun onSuccess(url: String)

        /**
         * Called when the upload fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Callback interface for file deletion operations.
     */
    interface DeleteCallback {
        /**
         * Called when the file(s) are successfully deleted.
         * @param boolean True if the operation succeeded.
         */
        fun onSuccess(boolean: Boolean)

        /**
         * Called when the deletion fails.
         * @param error The error that occurred.
         */
        fun onError(error: Error)
    }

    /**
     * Uploads a [File] to the specified bucket and path.
     * @param bucket The storage bucket name.
     * @param path The destination path in the bucket.
     * @param file The file to upload.
     * @param callback Callback for success or error.
     */
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

    /**
     * Uploads a file from a [Uri] to the specified bucket and path.
     * @param context Android context for resolving the URI.
     * @param bucket The storage bucket name.
     * @param path The destination path in the bucket.
     * @param uri The URI of the file to upload.
     * @param callback Callback for success or error.
     */
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

    /**
     * Uploads raw bytes to the specified bucket and path.
     * @param bucket The storage bucket name.
     * @param path The destination path in the bucket.
     * @param bytes The byte array to upload.
     * @param mimeType The MIME type of the file.
     * @param callback Callback for success or error.
     */
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

    /**
     * Deletes a single file from the specified bucket.
     * @param bucket The storage bucket name.
     * @param path The path of the file to delete.
     * @param callback Callback for success or error.
     */
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

    /**
     * Deletes multiple files from the specified bucket.
     * @param bucket The storage bucket name.
     * @param paths List of paths for the files to delete.
     * @param callback Callback for success or error.
     */
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