package br.com.natanaelribeiro.eventsdata.remote

import br.com.natanaelribeiro.eventsdata.contract.EventsDataContract
import br.com.natanaelribeiro.eventsdata.models.CheckInBody
import br.com.natanaelribeiro.eventsdata.models.Event
import br.com.natanaelribeiro.eventsdata.remote.endpoint.EventsApiClient
import kotlinx.coroutines.Deferred
import retrofit2.Response

class EventsRemoteDataSource(
    private val eventsApiClient: EventsApiClient
): EventsDataContract.Remote {

    override fun getEvents(): Deferred<Response<List<Event>>> = eventsApiClient.getEvents()

    override fun getEventDetails(eventId: String): Deferred<Response<Event>> {

        return eventsApiClient.getEventDetails(eventId)
    }

    override fun checkIn(eventId: String, name: String, email: String): Deferred<Response<Void>> {

        val checkInBody = CheckInBody(
            eventId = eventId,
            name = name,
            email = email
        )

        return eventsApiClient.checkIn(checkInBody)
    }
}