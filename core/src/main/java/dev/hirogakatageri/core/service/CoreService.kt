package dev.hirogakatageri.core.service

import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.newScope

/**
 * Based on ScopeService of Koin DI.
 * @ref
 * */
abstract class CoreService(
    private val initialiseScope: Boolean = true
) : LifecycleService(),
    KoinScopeComponent {

    override val scope: Scope by lazy { newScope(this) }

    override fun onCreate() {
        super.onCreate()

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