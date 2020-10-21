package com.hirogakatageri.home

import com.airbnb.epoxy.EpoxyController
import com.hirogakatageri.home.model.mainItemUser
import com.hirogakatageri.home.model.mainItemUserDark
import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.local.model.base.IUserModel
import com.hirogakatageri.utils.GenericEpoxyViewBindingClickListener

class HomeActivityMainController(
    private val clickListener: GenericEpoxyViewBindingClickListener
) : EpoxyController() {

    var models: List<IUserModel> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        models.forEachIndexed { index, localUserModel ->
            if (index % 4 == 0) {
                mainItemUserDark {
                    id(localUserModel.uid)
                    model(localUserModel)
                    onClickModel(clickListener::onClickEpoxyModel)
                }
            } else {
                mainItemUser {
                    id(localUserModel.uid)
                    model(localUserModel)
                    onClickModel(clickListener::onClickEpoxyModel)
                }
            }
        }
    }

}