package com.hirogakatageri.sandbox

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.hirogakatageri.base.BaseActivity
import com.hirogakatageri.sandbox.databinding.ActivityMainBinding
import java.util.*

class TestActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var controller: TestController

    private var currentList: List<EpoxyModel<*>> = emptyList()

    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override suspend fun ActivityMainBinding.bind() {
        controller = TestController()
        recyclerView.layoutManager =
            LinearLayoutManager(this@TestActivity, RecyclerView.VERTICAL, true)
        recyclerView.setController(controller)
        recyclerView.setItemSpacingRes(R.dimen.space_small)

        btnAdd.setOnClickListener {
            val random = 0..2
            val newList = mutableListOf<EpoxyModel<*>>()
            newList.addAll(currentList)
            newList.add(
                0,
                when (random.random()) {
                    0 -> TestText1EpoxyModel().id(UUID.randomUUID().toString())
                    1 -> TestText2EpoxyModel().id(UUID.randomUUID().toString())
                    2 -> TestText3EpoxyModel().id(UUID.randomUUID().toString())
                    else -> TestText1EpoxyModel()
                }
            )
            currentList = newList
            controller.models = currentList
            controller.requestModelBuild()
        }
    }
}