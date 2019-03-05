package com.hirogakatageri.sandbox.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.hirogakatageri.sandbox.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by org.kodein.di.android.kodein()

    /**
     * The layout resource Id.
     * */
    abstract val layoutResId: Int

    /**
     * Enables or Disables toolbar back button.
     * */
    abstract val withToolbarBackButton: Boolean

    /**
     * List of Fragments to show or hide.
     * */
    private var fragments: HashMap<String, Fragment> = hashMapOf()

    /**
     * Simple Class of Current Fragment.
     * */
    private var currentFragment: Fragment? = null

    /**
     * The toolbar.
     * */
    private val toolbar: Toolbar? by lazy {

        if (findViewById<Toolbar>(R.id.toolbar) != null) {
            findViewById<Toolbar>(R.id.toolbar)
        } else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        toolbar?.let {
            if (withToolbarBackButton) {
                it.setNavigationIcon(R.drawable.ic_arrow_back)
                it.setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

    /**
     * Compiles fragments to allow show and hide.
     * */
    fun compileFragments(list: List<Fragment>) {

        for (item in list) {
            fragments.put(item::class.java.simpleName, item)
        }

        fragments.let {
            for (item in it) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, item.value, item.key)
                    .hide(item.value)
                    .commit()
            }
        }
    }

    /**
     * Shows and hides Fragment in fragments hashmap.
     * */
    fun showFragment(fragmentClassName: String) {
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction()
                .show(
                    fragments[fragmentClassName]
                        ?: throw NullPointerException("Fragment does not exist.")
                )
                .hide(currentFragment!!)
                .commit()
            currentFragment = fragments[fragmentClassName]
        } else {
            currentFragment = fragments[fragmentClassName]

            supportFragmentManager.beginTransaction()
                .show(currentFragment!!)
                .commit()
        }
    }

    /**
     * @see <a href="https://antonioleiva.com/architecture-components-kotlin/">Antonio Leiva Architecture Components Kotlin</a>
     * @see <a href="https://antonioleiva.com/function-references-kotlin/">Function References</a>
     * */
    inline fun <reified V : ViewModel> FragmentActivity.getViewModel(): V =
        ViewModelProviders.of(this)[V::class.java]

    /**
     * @see <a href="https://antonioleiva.com/architecture-components-kotlin/">Antonio Leiva Architecture Components Kotlin</a>
     * @see <a href="https://antonioleiva.com/function-references-kotlin/">Function References</a>
     * */
    inline fun <reified V : ViewModel> FragmentActivity.withViewModel(body: V.() -> Unit): V {
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