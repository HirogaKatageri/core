package com.hirogakatageri.sandbox.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*

abstract class BaseFragment : Fragment() {

    /**
     * The layout resource Id.
     * */
    abstract val layoutResId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layoutResId, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * @see <a href="https://antonioleiva.com/architecture-components-kotlin/">Antonio Leiva Architecture Components Kotlin</a>
     * @see <a href="https://antonioleiva.com/function-references-kotlin/">Function References</a>
     * */
    inline fun <reified V : ViewModel> Fragment.getViewModel(): V =
        ViewModelProviders.of(this)[V::class.java]

    /**
     * @see <a href="https://antonioleiva.com/architecture-components-kotlin/">Antonio Leiva Architecture Components Kotlin</a>
     * @see <a href="https://antonioleiva.com/function-references-kotlin/">Function References</a>
     * */
    inline fun <reified V : ViewModel> Fragment.withViewModel(body: V.() -> Unit): V {
        val vm = getViewModel<V>()
        vm.body()
        return vm
    }

    /**
     * @see <a href="https://antonioleiva.com/architecture-components-kotlin/">Antonio Leiva Architecture Components Kotlin</a>
     * @see <a href="https://antonioleiva.com/function-references-kotlin/">Function References</a>
     * */
    fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
        liveData.observe(this, Observer(body))
    }

}