package br.com.natanaelribeiro.eventsdata.remote.endpoint

import br.com.natanaelribeiro.eventsdata.models.CheckInBody
import br.com.natanaelribeiro.eventsdata.models.Event
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsApiClient {

    @GET("events")
    fun getEvents(): Deferred<Response<List<Event>>>

    @GET("events/{eventId}")
    fun getEventDetails(
        @Path("eventId") eventId: String
    ): Deferred<Response<Event>>

    @POST("checkin")
    fun checkIn(
        @Body body: CheckInBody
    ): Deferred<Response<Void>>
}