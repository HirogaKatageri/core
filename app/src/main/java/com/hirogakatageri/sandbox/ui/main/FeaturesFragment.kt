package com.hirogakatageri.sandbox.ui.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hirogakatageri.sandbox.R
import com.hirogakatageri.sandbox.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_features.*

class FeaturesFragment : BaseFragment() {

    override val layoutResId: Int = R.layout.fragment_features

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        withViewModel<FeaturesViewModel> {
            observe(liveFragmentFeatures, ::createFeaturesAdapter)
        }

        getViewModel<FeaturesViewModel>().getFeatures()
    }

    private fun createFeaturesAdapter(list: List<String>?) {

        recycler_features.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler_features.adapter =
            FeaturesAdapter(list!!.map { name -> FeatureItem(featureName = name) }, context!!)

    }
}