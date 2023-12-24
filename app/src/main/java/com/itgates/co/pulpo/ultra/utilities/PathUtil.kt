package com.itgates.co.pulpo.ultra.utilities

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.net.URISyntaxException

/**
 * Created by Sheriff on 15/11/2022.
 */
object PathUtil {
    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi", "Recycle")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var myUri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (DocumentsContract.isDocumentUri(context.applicationContext, myUri)) {
            if (isExternalStorageDocument(myUri)) {
                val docId = DocumentsContract.getDocumentId(myUri)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(myUri)) {
                val id = DocumentsContract.getDocumentId(myUri)
                myUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(myUri)) {
                val docId = DocumentsContract.getDocumentId(myUri)
                val split = docId.split(":").toTypedArray()
                when (split[0]) {
                    "image" -> { myUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI }
                    "video" -> { myUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI }
                    "audio" -> { myUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI }
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(myUri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor =
                    context.contentResolver.query(myUri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (_: Exception) { }
        } else if ("file".equals(myUri.scheme, ignoreCase = true)) {
            return myUri.path
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}