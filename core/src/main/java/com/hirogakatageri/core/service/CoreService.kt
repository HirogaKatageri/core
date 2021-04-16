package com.hirogakatageri.core.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.OnLifecycleEvent
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.newScope
import kotlin.coroutines.CoroutineContext

/**
 * Based on ScopeService of Koin DI.
 * @ref
 * */
abstract class CoreService(
    private val initialiseScope: Boolean = true
) : LifecycleService(),
    CoroutineScope,
    LifecycleObserver,
    KoinScopeComponent {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    override val scope: Scope by lazy { newScope(this) }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun stopJobs() {
        d { "${this.javaClass.simpleName} destroyed." }
        job.cancelChildren(CancellationException("${this.javaClass.simpleName} destroyed."))
    }

    override fun onCreate() {
        super.onCreate()
        lifecycle.addObserver(this)

        if (initialiseScope) {
            getKoin().logger.debug("Open Service Scope: $scope")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        getKoin().logger.debug("Close service scope: $scope")
        scope.close()
    }

    /**
     * inject lazily
     * @param qualifier - bean qualifier / optional
     * @param mode
     * @param parameters - injection parameters
     */
    inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
        noinline parameters: ParametersDefinition? = null
    ) = lazy(mode) { get<T>(qualifier, parameters) }

    /**
     * get given dependency
     * @param name - bean name
     * @param scope
     * @param parameters - injection parameters
     */
    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
    ): T = scope.get(qualifier, parameters)
}