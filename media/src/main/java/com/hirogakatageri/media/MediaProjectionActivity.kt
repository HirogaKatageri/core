package com.hirogakatageri.media

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.BitmapFactory
import android.graphics.PixelFormat.RGBA_8888
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Surface
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.hirogakatageri.base.KodeinActivity
import kotlinx.android.synthetic.main.activity_media.*
import org.kodein.di.generic.instance
import java.io.File

private const val MEDIA_REQUEST = 100

class MediaProjectionActivity : KodeinActivity() {

    private val projectionManager: MediaProjectionManager by instance()
    private val handler: Handler = Handler()

    private lateinit var projection: MediaProjection

    private var requestComplete: Boolean = false

    private var _virtualDisplay: VirtualDisplay? = null

    override val layoutResId: Int
        get() = R.layout.activity_media

    override val toolbarId: Int
        get() = R.id.toolbar

    override val hasBackButton: Boolean = true

    override val titleResId: Int
        get() = R.string.app_name

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            MEDIA_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    projection = projectionManager.getMediaProjection(resultCode, data!!)
                    requestComplete = true
                    showRequestComplete()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_request.setOnClickListener {
            startActivityForResult(projectionManager.createScreenCaptureIntent(), MEDIA_REQUEST)
        }

        btn_capture.setOnClickListener {
            if (requestComplete) takeScreenshot()
            else showRequestNotYetComplete()
        }

        btn_preview.setOnClickListener {
            previewScreenshot()
        }
    }

    private fun showRequestComplete() {
        Snackbar.make(coordinator, R.string.msg_request_complete, LENGTH_SHORT)
            .setAction(android.R.string.ok) { _ -> }
            .show()
    }

    private fun showRequestNotYetComplete() {
        Snackbar.make(coordinator, R.string.error_request_not_done, LENGTH_SHORT)
            .setAction(android.R.string.ok) { _ -> }
            .show()
    }

    private fun takeScreenshot() {
        windowManager.defaultDisplay.let { display ->
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)

            val size = Point()
            display.getRealSize(size)

            val width = size.x
            val height = size.y
            val density = metrics.densityDpi

            val imageReader = ImageReader.newInstance(width, height, RGBA_8888, 2)

            val vDisplay = createVirtualDisplay("mirror", width, height, density, surface = imageReader.surface, handler = handler)

            imageReader.setOnImageAvailableListener({ reader ->
                reader.setOnImageAvailableListener(null, handler)

                val image = reader.acquireLatestImage()
                val planes = image.planes
                val buffer = planes[0].buffer

                val pixelStride = planes[0].pixelStride
                val rowStride = planes[0].rowStride
                val rowPadding = rowStride - pixelStride * metrics.widthPixels

                val bmp = Bitmap.createBitmap(metrics.widthPixels + (rowPadding / pixelStride), metrics.heightPixels, ARGB_8888)
                bmp.copyPixelsFromBuffer(buffer)

                vDisplay.release()
                _virtualDisplay = null
                image.close()
                reader.close()

                val realSizeBmp = Bitmap.createBitmap(bmp, 0, 0, metrics.widthPixels, bmp.height)
                bmp.recycle()

                openFileOutput("media.png", Context.MODE_PRIVATE)?.let { stream ->
                    realSizeBmp.compress(Bitmap.CompressFormat.PNG, 60, stream)
                    stream.close()
                    realSizeBmp.recycle()
                }

            }, handler)
        }
    }

    private fun createVirtualDisplay(
        name: String,
        width: Int,
        height: Int,
        dpi: Int,
        flags: Int = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
        surface: Surface?,
        callback: VirtualDisplay.Callback? = null,
        handler: Handler?
    ): VirtualDisplay =
        _virtualDisplay ?: projection.createVirtualDisplay(name, width, height, dpi, flags, surface, callback, handler).also {
            _virtualDisplay = it
        }

    private fun previewScreenshot() {
        filesDir.let { path ->
            val file = File(path, "media.png")

            if (file.exists()) {
                val bmp = BitmapFactory.decodeFile(file.absolutePath)
                img_preview.setImageBitmap(bmp)
            }
        }
    }

    companion object {

        fun start(context: Context) {
            Intent(context, MediaProjectionActivity::class.java).apply {
                context.startActivity(this)
            }
        }

    }
}