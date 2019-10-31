package com.hirogakatageri.base.util

import android.content.Context
import android.content.Intent

object IntentUtil {

    private const val BASE_PACKAGE = "com.hirogakatageri"

    fun createBlogListIntent(
        context: Context
    ): Intent? = createIntent(context, "blog.list", "BlogPostListActivity")

    private fun createIntent(
        context: Context,
        path: String,
        className: String
    ): Intent? = try {
        Class.forName("$BASE_PACKAGE.$path.$className")
    } catch (e: ClassNotFoundException) {
        null
    }?.toIntent(context)

    private fun Class<*>.toIntent(context: Context) = Intent(context, this)
}