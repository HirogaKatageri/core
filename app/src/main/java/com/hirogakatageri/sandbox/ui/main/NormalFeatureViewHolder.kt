package com.hirogakatageri.sandbox.ui.main

import android.view.View
import com.hirogakatageri.sandbox.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_feature_normal.view.*

class NormalFeatureViewHolder(itemView: View) : BaseViewHolder<FeatureItem>(itemView) {

    override fun bind(item: FeatureItem) {
        itemView.btn_feature.setText(item.featureName)
    }
}