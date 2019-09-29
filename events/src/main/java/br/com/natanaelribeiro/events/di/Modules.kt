package br.com.natanaelribeiro.events.di

import br.com.natanaelribeiro.events.viewmodels.ListOfEventsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val listOfEventsViewModelModule: Module = module {

    viewModel {
        ListOfEventsViewModel(
            eventsRepository = get(),
            coroutineContext = Dispatchers.IO,
            commandProvider = get()
        )
    }
}