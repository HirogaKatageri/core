package com.hirogakatageri.sandbox

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.hirogakatageri.base.KodeinActivity
import com.hirogakatageri.media.MediaProjectionActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_feature_normal.view.*
import org.hirogakatageri.rista.adapter.RistaAdapter
import org.hirogakatageri.rista.adapter.RistaConfig
import org.hirogakatageri.rista.adapter.RistaViewBinder
import org.hirogakatageri.rista.adapter.items.RistaItem

private const val DEFAULT_VIEW_TYPE = 100

class FeatureSelectionActivity : KodeinActivity() {

    private val ristaBinder: RistaViewBinder<RistaItem> = { holder, view, data ->

        view.button.text = when (data.itemId) {
            100L -> getString(R.string.media)
            else -> ""
        }

        view.button.setOnClickListener {
            when (data.itemId) {
                100L -> MediaProjectionActivity.start(this)
            }
        }
    }

    private val ristaConfig: RistaConfig = RistaConfig(false, ristaBinder).apply {
        addViewType(DEFAULT_VIEW_TYPE, R.layout.item_feature_normal)
    }
    private val adapter: RistaAdapter = RistaAdapter(ristaConfig)

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupFeatures()
    }

    private fun setupFeatures() {

        recycler_view.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        recycler_view.adapter = adapter

        adapter.addItem(RistaItem(DEFAULT_VIEW_TYPE, 100L))
    }

}