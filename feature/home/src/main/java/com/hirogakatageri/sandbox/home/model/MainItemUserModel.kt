package com.hirogakatageri.sandbox.home.model

import android.view.View
import androidx.core.view.isVisible
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.kotlin.ViewBindingEpoxyModelWithHolder
import com.hirogakatageri.sandbox.home.R
import com.hirogakatageri.sandbox.home.databinding.HomeMainItemUserBinding
import com.hirogakatageri.sandbox.home.model.base.IMainItemUser
import com.hirogakatageri.sandbox.local.model.base.IUserModel
import com.hirogakatageri.sandbox.utils.setTextSafely

@EpoxyModelClass
abstract class MainItemUserModel : ViewBindingEpoxyModelWithHolder<HomeMainItemUserBinding>(),
    IMainItemUser {

    @EpoxyAttribute
    override lateinit var model: IUserModel

    @EpoxyAttribute
    lateinit var onClickModel: View.OnClickListener

    override fun HomeMainItemUserBinding.bind() {
        container.setOnClickListener(onClickModel)
        imgProfile.load(model.profileImageUrl)
        txtUsername.setTextSafely(model.username)
        txtDescription.setTextSafely(model.htmlUrl)

        if(model.notes != null) imgNote.isVisible = true
        else imgNote.visibility = View.INVISIBLE
    }

    override fun getDefaultLayout(): Int = R.layout.home_main_item_user
}