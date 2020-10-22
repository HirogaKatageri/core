package com.hirogakatageri.home

import android.content.Intent
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ajalt.timberkt.d
import com.hirogakatageri.core.activity.BaseViewModelActivity
import com.hirogakatageri.home.databinding.HomeActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivityMain :
    BaseViewModelActivity<HomeActivityMainBinding, HomeActivityMainViewModel>() {

    override val viewModel: HomeActivityMainViewModel by viewModel()

    private val controller: HomeActivityMainController by inject {
        parametersOf(viewModel)
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
    }

    private val scrollListener: HomeActivityMainViewModel.HomeActivityEndlessScrollListener by lazy {
        viewModel.getScrollListener(layoutManager)
    }

    override fun createBinding(): HomeActivityMainBinding =
        HomeActivityMainBinding.inflate(layoutInflater)

    override suspend fun HomeActivityMainBinding.bind() {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemSpacingDp(12)
        recyclerView.setController(controller)

        viewModel.toStartActivity.observe {
            val intent = Intent(this@HomeActivityMain, Class.forName(it))
            startActivity(intent)
        }

        viewModel.network.observe { isAvailable ->
            txtNetworkStatus.isVisible = !isAvailable
            if (isAvailable) viewModel.retryQuery()
        }

        viewModel.isLoading.observe {
            controller.isLoading = it

            if (it) recyclerView.removeOnScrollListener(scrollListener).also {
                d { "Removing scroll listener..." }
            }
            else recyclerView.addOnScrollListener(scrollListener).also {
                d { "Adding scroll listener..." }
            }
        }

        viewModel.userList.observe {
            controller.models = it
        }

        txtSearch.doAfterTextChanged { text ->
            if (text?.isNotBlank() == true) {
                scrollListener.resetState()
                recyclerView.removeOnScrollListener(scrollListener).also {
                    d { "Removing scroll listener..." }
                }
            }

            viewModel.search(text.toString())

            if (text.isNullOrBlank())
                recyclerView.addOnScrollListener(scrollListener).also {
                    d { "Adding scroll listener..." }
                }
        }
    }
}