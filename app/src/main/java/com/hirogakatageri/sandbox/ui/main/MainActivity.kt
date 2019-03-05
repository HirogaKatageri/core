package com.hirogakatageri.sandbox.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hirogakatageri.sandbox.App
import com.hirogakatageri.sandbox.R
import com.hirogakatageri.sandbox.base.BaseActivity
import org.kodein.di.generic.instance

class MainActivity : BaseActivity() {

    private val fragmentList by instance<List<Fragment>>(App.FRAGMENTS)

    override val layoutResId = R.layout.activity_main
    override val withToolbarBackButton: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compileFragments(fragmentList)
        showFragment(FeaturesFragment::class.java.simpleName)
    }
}
