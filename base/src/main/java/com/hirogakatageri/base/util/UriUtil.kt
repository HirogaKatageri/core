package com.hirogakatageri.base.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object UriUtil {

    fun getFilePathFromFishbunUri(context: Context, uri: Uri): String? {
        val array = emptyArray<String>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, array, null, null, null)
            val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            return index?.let { cursor?.getString(it) }
        } finally {
            cursor?.close()
        }
    }

}