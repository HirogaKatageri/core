package com.hirogakatageri.home.model

import android.view.View
import androidx.core.view.isVisible
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.kotlin.ViewBindingEpoxyModelWithHolder
import com.hirogakatageri.home.R
import com.hirogakatageri.home.databinding.HomeMainItemUserBinding
import com.hirogakatageri.home.model.base.IMainItemUser
import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.local.model.base.IUserModel
import com.hirogakatageri.utils.setTextSafely

@EpoxyModelClass
abstract class MainItemUserModel : ViewBindingEpoxyModelWithHolder<HomeMainItemUserBinding>(),
    IMainItemUser {

    @EpoxyAttribute
    override lateinit var model: IUserModel

    @EpoxyAttribute
    lateinit var onClickModel: View.OnClickListener

    override fun HomeMainItemUserBinding.bind() {
        imgProfile.load(model.profileImageUrl)
        txtUsername.setTextSafely(model.username)
        txtDescription.setTextSafely(model.htmlUrl)

        if(model.notes != null) imgNote.isVisible = true
        else imgNote.visibility = View.INVISIBLE
    }

    override fun getDefaultLayout(): Int = R.layout.home_main_item_user
}