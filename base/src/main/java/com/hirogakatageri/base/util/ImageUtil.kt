package com.hirogakatageri.base.util

import android.content.Context
import android.graphics.Bitmap
import id.zelory.compressor.Compressor
import java.io.File

object ImageUtil {

    fun compress(context: Context, newFileName: String, fileToCompress: File): File =
        Compressor(context).setMaxWidth(1280)
            .setMaxHeight(720)
            .setQuality(80)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .setDestinationDirectoryPath(context.filesDir.absolutePath)
            .compressToFile(fileToCompress, newFileName)

}