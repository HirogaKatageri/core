package com.hirogakatageri.core.sample.ui.main

sealed class SampleState {

    class Created : SampleState() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class TimeUpdated(val time: String) : SampleState()

}
