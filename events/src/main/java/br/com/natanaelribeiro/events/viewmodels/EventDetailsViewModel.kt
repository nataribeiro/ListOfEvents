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

class EventDetailsViewModel(
    private val eventsRepository: EventsRepository,
    private val coroutineContext: CoroutineContext,
    private val commandProvider: CommandProvider
): ViewModel() {

    val command: SingleLiveEvent<GenericCommand> = commandProvider.getCommand()
    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    val coroutineScope = CoroutineScope(coroutineContext)

    data class ViewState(
        val isLoadingEvent: Boolean = false,
        val isDoingCheckIn: Boolean = false
    )

    sealed class Command: GenericCommand() {

        object ShowErrorDoingCheckIn: Command()
        object ShowErrorLoadingEvent: Command()
        class ShowEventDetails(val event: Event): Command()
        object ShowSuccessCheckInMessage: Command()
    }

    init {

        viewState.setValue(ViewState())
    }

    fun getEventDetails(eventId: String) {

        viewState.setValue(currentViewState().copy(isLoadingEvent = true))

        coroutineScope.launch {

            try {

                val response = eventsRepository.getEventDetails(eventId).await()

                viewState.postValue(currentViewState().copy(isLoadingEvent = false))

                if (response.isSuccessful) {

                    response.body()?.let { event ->

                        command.postValue(Command.ShowEventDetails(event))
                    }
                } else {

                    command.postValue(Command.ShowErrorLoadingEvent)
                }

            } catch (e: Exception) {

                viewState.postValue(currentViewState().copy(isLoadingEvent = false))
                command.postValue(Command.ShowErrorLoadingEvent)
            }
        }
    }

    fun checkIn(eventId: String, name: String, email: String) {

        viewState.setValue(currentViewState().copy(isDoingCheckIn = true))

        coroutineScope.launch {

            try {

                val response = eventsRepository.checkIn(eventId, name, email).await()

                viewState.postValue(currentViewState().copy(isDoingCheckIn = false))

                if (response.isSuccessful) {

                    command.postValue(Command.ShowSuccessCheckInMessage)
                } else {

                    command.postValue(Command.ShowErrorDoingCheckIn)
                }

            } catch (e: Exception) {

                viewState.postValue(currentViewState().copy(isDoingCheckIn = false))
                command.postValue(Command.ShowErrorDoingCheckIn)
            }
        }
    }

    private fun currentViewState(): ViewState = viewState.value!!
}