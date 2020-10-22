package com.hirogakatageri.home

import com.airbnb.epoxy.EpoxyController
import com.hirogakatageri.home.model.mainItemLoading
import com.hirogakatageri.home.model.mainItemUser
import com.hirogakatageri.home.model.mainItemUserDark
import com.hirogakatageri.local.model.base.IUserModel
import com.hirogakatageri.utils.GenericEpoxyViewBindingClickListener

class HomeActivityMainController(
    private val clickListener: GenericEpoxyViewBindingClickListener
) : EpoxyController() {

    var isLoading: Boolean = false
        set(value) {
            field = value
            if (value) areModelsUpdated = false
            if (!areModelsUpdated) requestModelBuild()
        }

    var models: List<IUserModel> = emptyList()
        set(value) {
            field = value
            areModelsUpdated = true
            isLoading = false
            requestModelBuild()
        }

    private var areModelsUpdated: Boolean = false

    override fun buildModels() {
        models.forEachIndexed { index, localUserModel ->
            if ((index + 1) % 4 == 0) {
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

        if (isLoading) mainItemLoading {
            id("loading")
        }
    }

}