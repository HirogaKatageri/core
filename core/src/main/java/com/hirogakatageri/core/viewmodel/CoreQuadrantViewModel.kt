package com.hirogakatageri.core.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.hirogakatageri.core.utils.livedata.SingleLiveEvent

abstract class CoreQuadrantViewModel : CoreViewModel() {

    protected val _quadrant: SingleLiveEvent<Pair<String, Bundle>> = SingleLiveEvent()
    val quadrant: LiveData<Pair<String, Bundle>> = _quadrant

}