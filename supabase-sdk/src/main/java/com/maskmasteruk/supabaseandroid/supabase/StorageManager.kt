package com.maskmasteruk.supabaseandroid.supabase

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import java.io.File

object StorageManager {
    private fun client() = AuthManager.getClient()

    suspend fun upload(
        bucket: String,
        path: String,
        file: File
    ): String {
        client().storage.from(bucketId = bucket).upload(path, file.readBytes())

        return client().storage.from(bucket).publicUrl(path)
    }

    suspend fun upload(
        context: Context,
        bucket: String,
        path: String,
        uri: Uri
    ): String {

        val bytes = context.contentResolver.openInputStream(uri)
            ?.use { it.readBytes() }
            ?: throw Exception("Unable to read file")

        val mimeType = context.contentResolver.getType(uri)

        val contentTypeValue = mimeType?.let {
            ContentType.parse(it)
        } ?: ContentType.Application.OctetStream

        client().storage.from(bucketId = bucket).upload(path, bytes) {
            contentType = contentTypeValue
        }

        return client().storage.from(bucket).publicUrl(path)
    }

    suspend fun upload(
        bucket: String,
        path: String,
        bytes: ByteArray,
        mimeType: String?
    ): String {
        val contentTypeValue = mimeType?.let {
            ContentType.parse(it)
        } ?: ContentType.Application.OctetStream

        client().storage.from(bucketId = bucket).upload(path, bytes) {
            contentType = contentTypeValue
        }

        return client().storage.from(bucket).publicUrl(path)
    }

    suspend fun delete(
        bucket: String,
        path: String
    ) {
        client()
            .storage
            .from(bucket)
            .delete(path)
    }

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