package br.com.natanaelribeiro.events.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.natanaelribeiro.basefeature.interfaces.CommandProvider
import br.com.natanaelribeiro.basefeature.models.GenericCommand
import br.com.natanaelribeiro.basefeature.viewmodels.SingleLiveEvent
import br.com.natanaelribeiro.eventsdata.contract.repositories.EventsRepository
import br.com.natanaelribeiro.eventsdata.models.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListOfEventsViewModel(
    private val eventsRepository: EventsRepository,
    private val coroutineContext: CoroutineContext,
    private val commandProvider: CommandProvider
): ViewModel() {

    val command: SingleLiveEvent<GenericCommand> = commandProvider.getCommand()
    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    val coroutineScope = CoroutineScope(coroutineContext)

    data class ViewState(
        val isLoadingEvents: Boolean = false
    )

    sealed class Command: GenericCommand() {

        class ShowEventDetails(val eventId: String): Command()
        object ShowErrorLoadingList: Command()
        class ShowListOfEvents(val eventsList: List<Event>): Command()
    }

    init {

        viewState.setValue(ViewState())
    }

    fun getEvents() {

        viewState.setValue(currentViewState().copy(isLoadingEvents = true))

        coroutineScope.launch {

            try {

                val response = eventsRepository.getEvents().await()

                viewState.postValue(currentViewState().copy(isLoadingEvents = false))

                if (response.isSuccessful) {

                    response.body()?.let { eventsList ->

                        command.postValue(Command.ShowListOfEvents(eventsList))
                    }
                } else {

                    command.postValue(Command.ShowErrorLoadingList)
                }

            } catch (e: Exception) {

                viewState.postValue(currentViewState().copy(isLoadingEvents = false))
                command.postValue(Command.ShowErrorLoadingList)
            }
        }
    }

    private fun currentViewState(): ViewState = viewState.value!!
}