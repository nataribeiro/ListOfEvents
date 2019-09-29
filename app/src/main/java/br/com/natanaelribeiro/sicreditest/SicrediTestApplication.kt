package br.com.natanaelribeiro.sicreditest

import android.app.Application
import br.com.natanaelribeiro.basefeature.di.commandInjectorModule
import br.com.natanaelribeiro.events.di.listOfEventsViewModelModule
import br.com.natanaelribeiro.eventsdata.di.evensDataRepositoryModule
import br.com.natanaelribeiro.eventsdata.di.eventsDataNetworkModule
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SicrediTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@SicrediTestApplication)

            modules(
                listOf(
                    commandInjectorModule,
                    eventsDataNetworkModule,
                    evensDataRepositoryModule,
                    listOfEventsViewModelModule
                )
            )
        }

        Picasso.setSingletonInstance(Picasso.Builder(this).build())
    }
}