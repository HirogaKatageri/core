package com.hirogakatageri.sandbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.hirogakatageri.sandbox.databinding.ActivityMainBinding
import java.util.*

class TestActivity : AppCompatActivity() {

    private lateinit var controller: TestController
    private lateinit var binding: ActivityMainBinding

    private var currentList: List<EpoxyModel<*>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = TestController()
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        binding.recyclerView.setController(controller)
        binding.recyclerView.setItemSpacingRes(R.dimen.space_small)

        binding.btnAdd.setOnClickListener {
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