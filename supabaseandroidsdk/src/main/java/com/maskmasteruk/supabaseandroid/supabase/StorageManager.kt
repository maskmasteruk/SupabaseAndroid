package com.maskmasteruk.supabaseandroid.supabase

import android.content.Context
import android.net.Uri
import com.maskmasteruk.supabaseandroid.objects.SupabaseError
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import java.io.File

/**
 * Singleton manager for handling Supabase Storage operations.
 */
object StorageManager {
    private fun client() = AuthManager.getClient()

    /**
     * Uploads a [File] to storage.
     * @param bucket The bucket ID.
     * @param path The file path in the bucket.
     * @param file The file to upload.
     * @param upsert Whether to overwrite the file if it already exists.
     * @return The public URL of the uploaded file.
     */
    suspend fun upload(
        bucket: String,
        path: String,
        file: File,
        upsert: Boolean = false
    ): String {
        client().storage.from(bucketId = bucket).upload(path, file.readBytes()) {
            this.upsert = upsert
        }

        return client().storage.from(bucket).publicUrl(path)
    }

    /**
     * Uploads a file from a [Uri] to storage.
     * @param context Android context for resolving the URI and reading bytes.
     * @param bucket The bucket ID.
     * @param path The file path in the bucket.
     * @param uri The URI of the file.
     * @param upsert Whether to overwrite the file if it already exists.
     * @return The public URL of the uploaded file.
     * @throws Exception if the file cannot be read.
     */
    suspend fun upload(
        context: Context,
        bucket: String,
        path: String,
        uri: Uri,
        upsert: Boolean = false
    ): String {

        val bytes = context.contentResolver.openInputStream(uri)
            ?.use { it.readBytes() }
            ?: throw SupabaseError("Unable to read file")

        val mimeType = context.contentResolver.getType(uri)

        val contentTypeValue = mimeType?.let {
            ContentType.parse(it)
        } ?: ContentType.Application.OctetStream

        client().storage.from(bucketId = bucket).upload(path, bytes) {
            contentType = contentTypeValue
            this.upsert = upsert
        }

        return client().storage.from(bucket).publicUrl(path)
    }

    /**
     * Uploads raw bytes to storage.
     * @param bucket The bucket ID.
     * @param path The file path in the bucket.
     * @param bytes The raw data.
     * @param mimeType The MIME type of the data.
     * @param upsert Whether to overwrite the file if it already exists.
     * @return The public URL of the uploaded file.
     */
    suspend fun upload(
        bucket: String,
        path: String,
        bytes: ByteArray,
        mimeType: String?,
        upsert: Boolean = false
    ): String {
        val contentTypeValue = mimeType?.let {
            ContentType.parse(it)
        } ?: ContentType.Application.OctetStream

        client().storage.from(bucketId = bucket).upload(path, bytes) {
            contentType = contentTypeValue
            this.upsert = upsert
        }

        return client().storage.from(bucket).publicUrl(path)
    }

    /**
     * Deletes a single file from storage.
     * @param bucket The bucket ID.
     * @param path The file path to delete.
     */
    suspend fun delete(
        bucket: String,
        path: String
    ) {
        client()
            .storage
            .from(bucket)
            .delete(path)
    }

    /**
     * Deletes multiple files from storage.
     * @param bucket The bucket ID.
     * @param paths List of file paths to delete.
     */
    suspend fun delete(
        bucket: String,
        paths: List<String>
    ) {
        client()
            .storage
            .from(bucket)
            .delete(paths)
    }
}