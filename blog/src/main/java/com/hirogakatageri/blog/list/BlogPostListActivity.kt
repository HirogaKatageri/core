package com.hirogakatageri.blog.list

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hirogakatageri.base.BaseActivity
import com.hirogakatageri.base.ext.observe
import com.hirogakatageri.base.util.ImageUtil
import com.hirogakatageri.base.util.UriUtil
import com.hirogakatageri.blog.R
import com.hirogakatageri.blog.list.item.BlogPostItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.DiffCallback
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.sangcomz.fishbun.define.Define
import kotlinx.android.synthetic.main.activity_blog.*
import kotlinx.android.synthetic.main.layout_add_post.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.*
import java.io.File

@RuntimePermissions
class BlogPostListActivity : BaseActivity<BlogPostListViewModel>() {

    private val itemAdapter = ItemAdapter<BlogPostItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    private var selectedImages: ArrayList<Uri> = ArrayList()

    override val titleResId: Int?
        get() = R.string.blog_list
    override val layoutResId: Int?
        get() = R.layout.activity_blog
    override val model: BlogPostListViewModel by viewModel()

    override fun loadContent() {
        model.getPosts()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBlogPostsObserver()
        setupImageUploadObserver()
        setupPostObserver()
        setupGallerySelectionButton()
        setupCreatePost()
        setupAdapter()
    }

    private fun setupAdapter() {
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = fastAdapter
    }

    private fun setupBlogPostsObserver() {
        model.blogPosts.observe(this) { blogPosts ->
            val items = blogPosts.map { BlogPostItem(it) }
            FastAdapterDiffUtil.set(itemAdapter, items.sortedByDescending { item -> item.model.timestamp }, BlogPostDiffCallback())
        }
    }

    private fun setupImageUploadObserver() {
        model.isUploadSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) Snackbar.make(coordinator, R.string.success_image_upload, Snackbar.LENGTH_SHORT).show()
            else Snackbar.make(coordinator, R.string.error_image_upload, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupPostObserver() {
        model.isPostSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) Snackbar.make(coordinator, R.string.success_post, Snackbar.LENGTH_SHORT).show()
            else Snackbar.make(coordinator, R.string.error_post, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupGallerySelectionButton() {
        img_gallery.setOnClickListener { showGalleryWithPermissionCheck() }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showGallery() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setSelectedImages(selectedImages)
            .setAllViewTitle(getString(R.string.blog_gallery_title_all))
            .setActionBarTitle(getString(R.string.blog_gallery_title_default))
            .startAlbum()
    }

    private fun showGalleryPermissionError() {
        Snackbar.make(coordinator, R.string.error_permission_denied_add_photos, Snackbar.LENGTH_SHORT).show()
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForGallery(request: PermissionRequest) {
        showGalleryPermissionError()
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPhotosFromGalleryDenied() {
        showGalleryPermissionError()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPhotosFromGalleryNeverAskAgain() {
        showGalleryPermissionError()
    }

    private fun setupCreatePost() {
        txt_input_message.setOnEditorActionListener { textView, i, keyEvent ->
            model.submitPost(txt_input_message.text.toString())
            txt_input_message.text = null
            model.imageUri = null
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Define.ALBUM_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    data?.getParcelableArrayListExtra<Uri>(Define.INTENT_PATH)?.let { list ->
                        selectedImages = list
                        val uri = selectedImages[0]
                        val path = UriUtil.getFilePathFromFishbunUri(this, uri)
                        val compressedFile = ImageUtil.compress(this, "mini-file", File(path))
                        model.uploadImage(compressedFile)
                    }
                }
            }
        }
    }

    class BlogPostDiffCallback : DiffCallback<BlogPostItem> {
        override fun areContentsTheSame(oldItem: BlogPostItem, newItem: BlogPostItem): Boolean =
            oldItem.model.imageUrl == newItem.model.imageUrl &&
                    oldItem.model.message == newItem.model.message &&
                    oldItem.model.private == newItem.model.private

        override fun areItemsTheSame(oldItem: BlogPostItem, newItem: BlogPostItem): Boolean =
            oldItem.model.id == newItem.model.id

        override fun getChangePayload(oldItem: BlogPostItem, oldItemPosition: Int, newItem: BlogPostItem, newItemPosition: Int): Any? {
            val diffBundle = Bundle()

            if (oldItem.model.imageUrl != newItem.model.imageUrl)
                diffBundle.putString(BlogPostItem.IMAGE_URL, newItem.model.imageUrl)

            if (oldItem.model.message != newItem.model.message)
                diffBundle.putString(BlogPostItem.MESSAGE, newItem.model.message)

            return if (diffBundle.isEmpty) null else diffBundle
        }
    }
}