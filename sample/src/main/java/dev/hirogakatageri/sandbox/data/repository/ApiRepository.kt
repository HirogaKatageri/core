package dev.hirogakatageri.sandbox.data.repository

import dev.hirogakatageri.sandbox.data.payload.auth.VerifyUserPayload
import dev.hirogakatageri.sandbox.data.service.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository(
    private val api: ApiService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun verifyUser(
        idToken: String?
    ) = withContext(defaultDispatcher) {
        val payload = VerifyUserPayload(idToken)
        api.verifyUser(payload)
    }
}
