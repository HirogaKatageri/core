package com.hirogakatageri.sandbox.ui.main

import android.content.Context
import android.view.ViewGroup
import com.hirogakatageri.sandbox.R
import com.hirogakatageri.sandbox.base.BaseAdapter
import com.hirogakatageri.sandbox.base.BaseViewHolder
import com.hirogakatageri.sandbox.extensions.inflate

class FeaturesAdapter(
    override val list: List<FeatureItem>,
    context: Context
) : BaseAdapter<BaseViewHolder<FeatureItem>, FeatureItem>(context) {

    override fun onBindViewHolder(holder: BaseViewHolder<FeatureItem>, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FeatureItem> =
        when (viewType) {
            FeatureItem.TYPE_NORMAL -> NormalFeatureViewHolder(parent.inflate(R.layout.item_feature_normal))
            else -> NormalFeatureViewHolder(parent.inflate(R.layout.item_feature_normal))
        }

}