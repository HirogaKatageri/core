package com.hirogakatageri.core.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class SampleViewModel : ViewModel() {

    private val _dateTimeObservable: MutableLiveData<LocalDateTime> = MutableLiveData()
    val dateTimeObservable: LiveData<LocalDateTime> = _dateTimeObservable

    private lateinit var ticker: ReceiveChannel<Unit>

    suspend fun startTimer() = viewModelScope.launch {
        ticker = ticker(
            delayMillis = 1000,
            context = Dispatchers.IO
        )

        for (event in ticker) {
            _dateTimeObservable.postValue(LocalDateTime.now())
        }
    }

    suspend fun stopTimer() = viewModelScope.launch {
        ticker.cancel()
    }

}