package com.hirogakatageri.core.sample

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import com.hirogakatageri.core.fragment.CoreViewModelFragment
import com.hirogakatageri.core.sample.databinding.FragmentSampleBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SampleFragment : CoreViewModelFragment<FragmentSampleBinding, SampleViewModel>() {

    override val vm: SampleViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentSampleBinding =
        FragmentSampleBinding.inflate(layoutInflater, container, false)

    override fun FragmentSampleBinding.bind() {
        vm.dateTimeObservable.observe(viewLifecycleOwner) { dateTime ->
            textView.text = "$dateTime"
        }
    }

    @OnLifecycleEvent(Event.ON_RESUME)
    private fun startTimer() = launch {
        vm.startTimer()
    }

    @OnLifecycleEvent(Event.ON_PAUSE)
    private fun pauseTimer() = launch {
        vm.stopTimer()
    }
}