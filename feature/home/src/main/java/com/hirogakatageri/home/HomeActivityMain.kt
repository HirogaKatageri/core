package com.hirogakatageri.home

import androidx.core.widget.doAfterTextChanged
import com.hirogakatageri.core.activity.BaseViewModelActivity
import com.hirogakatageri.home.databinding.HomeActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivityMain :
    BaseViewModelActivity<HomeActivityMainBinding, HomeActivityMainViewModel>() {

    override val viewModel: HomeActivityMainViewModel by viewModel()

    private val controller: HomeActivityMainController by inject {
        parametersOf(
            viewModel
        )
    }

    override fun createBinding(): HomeActivityMainBinding =
        HomeActivityMainBinding.inflate(layoutInflater)

    override suspend fun HomeActivityMainBinding.bind() {
        recyclerView.setItemSpacingDp(12)
        recyclerView.setController(controller)

        viewModel.userList.observe {
            controller.models = it
        }

        txtSearch.doAfterTextChanged { text -> viewModel.search(text.toString()) }
    }
}