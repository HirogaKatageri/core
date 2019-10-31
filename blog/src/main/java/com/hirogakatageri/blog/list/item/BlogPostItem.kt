package com.hirogakatageri.blog.list.item

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.hirogakatageri.base.GlideApp
import com.hirogakatageri.blog.R
import com.hirogakatageri.data.base.BlogPost
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class BlogPostItem(val model: BlogPost) : AbstractItem<BlogPostItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_blog_post
    override val type: Int
        get() = R.id.type_blog_post

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<BlogPostItem>(itemView) {

        val imgHeader: ImageView get() = itemView.findViewById(R.id.img_header)
        val txtMsg: TextView get() = itemView.findViewById(R.id.txt_message)

        override fun bindView(item: BlogPostItem, payloads: MutableList<Any>) {

            if (payloads.isEmpty()) {
                item.model.imageUrl?.let { url ->
                    GlideApp.with(itemView)
                        .load(item.model.imageUrl)
                        .into(imgHeader)

                    imgHeader.isVisible = true
                } ?: { imgHeader.isVisible = false }()

                txtMsg.text = item.model.message
            } else {
                val bundle = payloads[0] as Bundle
                parsePayload(bundle)
            }
        }

        private fun parsePayload(bundle: Bundle) {
            bundle.getString(IMAGE_URL)?.let {
                GlideApp.with(itemView)
                    .load(it)
                    .into(imgHeader)

                imgHeader.isVisible = true
            }

            bundle.getString(MESSAGE)?.let { txtMsg.text = it }
        }

        override fun unbindView(item: BlogPostItem) {
            imgHeader.setImageDrawable(null)
            txtMsg.text = null
        }
    }

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
        const val MESSAGE = "MESSAGE"
    }

}