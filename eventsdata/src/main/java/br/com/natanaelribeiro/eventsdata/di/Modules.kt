package br.com.natanaelribeiro.eventsdata.di

import br.com.natanaelribeiro.coredata.BuildConfig
import br.com.natanaelribeiro.coredata.api.RestClient
import br.com.natanaelribeiro.eventsdata.contract.EventsDataContract
import br.com.natanaelribeiro.eventsdata.contract.repositories.EventsRepository
import br.com.natanaelribeiro.eventsdata.remote.EventsRemoteDataSource
import br.com.natanaelribeiro.eventsdata.remote.endpoint.EventsApiClient
import br.com.natanaelribeiro.eventsdata.repositories.EventsDataRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val eventsDataNetworkModule: Module = module {

    factory<EventsDataContract.Remote> {
        EventsRemoteDataSource(
            eventsApiClient = get()
        )
    }

    single {
        RestClient.getApiClient(
            serviceClass = EventsApiClient::class.java,
            baseEndpoint = BuildConfig.SICREDI_TEST_URL
        )
    }
}

val evensDataRepositoryModule: Module = module {

    factory<EventsRepository> {
        EventsDataRepository(
            eventsRemoteDataSource = get()
        )
    }
}