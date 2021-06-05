/*
 *    Copyright 2021 Gian Patrick Quintana
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.hirogakatageri.core

import android.app.Application
import androidx.annotation.Keep
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinApplication
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@Keep
abstract class CoreApp : Application() {

    protected abstract val moduleList: List<Module>

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()

        startKoin {
            onStartKoin()
            androidContext(this@CoreApp)
            androidFileProperties()
            fragmentFactory()
            modules(moduleList)
        }
    }

    protected open fun KoinApplication.onStartKoin() {}
}