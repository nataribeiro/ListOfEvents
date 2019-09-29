package br.com.natanaelribeiro.eventsdata.contract

import br.com.natanaelribeiro.eventsdata.models.Event
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface EventsDataContract {

    interface Remote {

        fun getEvents(): Deferred<Response<List<Event>>>

        fun getEventDetails(eventId: String): Deferred<Response<Event>>

        fun checkIn(
            eventId: String,
            name: String,
            email: String
        ): Deferred<Response<Void>>
    }
}