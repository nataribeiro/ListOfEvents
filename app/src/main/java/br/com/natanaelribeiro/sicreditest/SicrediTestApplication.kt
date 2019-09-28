package br.com.natanaelribeiro.sicreditest

import android.app.Application
import br.com.natanaelribeiro.eventsdata.di.evensDataRepositoryModule
import br.com.natanaelribeiro.eventsdata.di.eventsDataNetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SicrediTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@SicrediTestApplication)

            modules(
                listOf(
                    eventsDataNetworkModule,
                    evensDataRepositoryModule
                )
            )
        }
    }
}