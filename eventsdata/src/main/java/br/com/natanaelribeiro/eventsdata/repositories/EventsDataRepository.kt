package br.com.natanaelribeiro.eventsdata.repositories

import br.com.natanaelribeiro.eventsdata.contract.EventsDataContract
import br.com.natanaelribeiro.eventsdata.contract.repositories.EventsRepository
import br.com.natanaelribeiro.eventsdata.models.Event
import kotlinx.coroutines.Deferred
import retrofit2.Response

class EventsDataRepository(
    private val eventsRemoteDataSource: EventsDataContract.Remote
): EventsRepository {

    override fun getEvents(): Deferred<Response<List<Event>>> = eventsRemoteDataSource.getEvents()

    override fun getEventDetails(eventId: String): Deferred<Response<Event>> =
        eventsRemoteDataSource.getEventDetails(eventId)

    override fun checkIn(eventId: String, name: String, email: String): Deferred<Response<Void>> {
        return checkIn(eventId, name, email)
    }
}