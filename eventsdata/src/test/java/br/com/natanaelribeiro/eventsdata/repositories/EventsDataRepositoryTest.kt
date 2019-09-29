package br.com.natanaelribeiro.eventsdata.repositories

import br.com.natanaelribeiro.eventsdata.contract.EventsDataContract
import br.com.natanaelribeiro.eventsdata.contract.repositories.EventsRepository
import br.com.natanaelribeiro.eventsdata.models.Coupon
import br.com.natanaelribeiro.eventsdata.models.Event
import br.com.natanaelribeiro.eventsdata.models.Person
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class EventsDataRepositoryTest {

    private lateinit var eventsRemoteDataSourceMock: EventsDataContract.Remote

    private lateinit var eventsRepository: EventsRepository

    @Before fun setUp(){

        eventsRemoteDataSourceMock = mock()

        eventsRepository = EventsDataRepository(
            eventsRemoteDataSourceMock
        )
    }

    @Test fun `Get events, when is requested, then returns deferred list of events`() {

        // ARRANGE

        val EXPECTED_PERSON_ID_1 = "1"
        val EXPECTED_PERSON_EVENT_ID_1 = "1"
        val EXPECTED_PERSON_NAME_1 = "name 1"
        val EXPECTED_PERSON_PICTURE_1 = "picture 1"

        val EXPECTED_COUPON_ID_1 = "1"
        val EXPECTED_COUPON_EVENT_ID_1 = "1"
        val EXPECTED_COUPON_DISCOUNT_1 = 62

        val EXPECTED_EVENT_DATE = 1534784400000
        val EXPECTED_EVENT_DESCRIPTION = "Description"
        val EXPECTED_EVENT_IMAGE = "Image"
        val EXPECTED_EVENT_LONGITUDE = "-51.2146267"
        val EXPECTED_EVENT_LATITUDE = "-30.0392981"
        val EXPECTED_EVENT_PRICE = 29.99
        val EXPECTED_EVENT_TITLE = "Title"
        val EXPECTED_EVENT_ID = "1"

        val expectedPerson1 = Person(
            id = EXPECTED_PERSON_ID_1,
            eventId = EXPECTED_PERSON_EVENT_ID_1,
            name = EXPECTED_PERSON_NAME_1,
            picture = EXPECTED_PERSON_PICTURE_1
        )

        val expectedCoupon1 = Coupon(
            id = EXPECTED_COUPON_ID_1,
            eventId = EXPECTED_COUPON_EVENT_ID_1,
            discount = EXPECTED_COUPON_DISCOUNT_1
        )

        val expectedEvent1 = Event(
            people = listOf(expectedPerson1),
            date = EXPECTED_EVENT_DATE,
            description = EXPECTED_EVENT_DESCRIPTION,
            imageUrl = EXPECTED_EVENT_IMAGE,
            longitude = EXPECTED_EVENT_LONGITUDE,
            latitude = EXPECTED_EVENT_LATITUDE,
            price = EXPECTED_EVENT_PRICE,
            title = EXPECTED_EVENT_TITLE,
            id = EXPECTED_EVENT_ID,
            cupons = listOf(expectedCoupon1)
        )

        val expectedListOfEvents = listOf(expectedEvent1)

        val expectedListOfEventsResponse = Response.success(expectedListOfEvents)

        val expectedDeferredListOfEvents = CompletableDeferred(expectedListOfEventsResponse)

        whenever(eventsRemoteDataSourceMock.getEvents())
            .thenReturn(expectedDeferredListOfEvents)

        // ACT

        val listOfEventsResponse = runBlocking {
            eventsRepository.getEvents().await()
        }

        // ASSERT

        assertEquals(expectedListOfEventsResponse, listOfEventsResponse)
    }

    @Test fun `Get events details, when it is passed an eventId, then returns deferred Event`() {

        // ARRANGE

        val EVENT_ID = "1"

        val EXPECTED_PERSON_ID_1 = "1"
        val EXPECTED_PERSON_EVENT_ID_1 = "1"
        val EXPECTED_PERSON_NAME_1 = "name 1"
        val EXPECTED_PERSON_PICTURE_1 = "picture 1"

        val EXPECTED_COUPON_ID_1 = "1"
        val EXPECTED_COUPON_EVENT_ID_1 = "1"
        val EXPECTED_COUPON_DISCOUNT_1 = 62

        val EXPECTED_EVENT_DATE = 1534784400000
        val EXPECTED_EVENT_DESCRIPTION = "Description"
        val EXPECTED_EVENT_IMAGE = "Image"
        val EXPECTED_EVENT_LONGITUDE = "-51.2146267"
        val EXPECTED_EVENT_LATITUDE = "-30.0392981"
        val EXPECTED_EVENT_PRICE = 29.99
        val EXPECTED_EVENT_TITLE = "Title"
        val EXPECTED_EVENT_ID = "1"

        val expectedPerson1 = Person(
            id = EXPECTED_PERSON_ID_1,
            eventId = EXPECTED_PERSON_EVENT_ID_1,
            name = EXPECTED_PERSON_NAME_1,
            picture = EXPECTED_PERSON_PICTURE_1
        )

        val expectedCoupon1 = Coupon(
            id = EXPECTED_COUPON_ID_1,
            eventId = EXPECTED_COUPON_EVENT_ID_1,
            discount = EXPECTED_COUPON_DISCOUNT_1
        )

        val expectedEventDetail = Event(
            people = listOf(expectedPerson1),
            date = EXPECTED_EVENT_DATE,
            description = EXPECTED_EVENT_DESCRIPTION,
            imageUrl = EXPECTED_EVENT_IMAGE,
            longitude = EXPECTED_EVENT_LONGITUDE,
            latitude = EXPECTED_EVENT_LATITUDE,
            price = EXPECTED_EVENT_PRICE,
            title = EXPECTED_EVENT_TITLE,
            id = EXPECTED_EVENT_ID,
            cupons = listOf(expectedCoupon1)
        )

        val expectedEventDetailResponse = Response.success(expectedEventDetail)

        val expectedDeferredEvent = CompletableDeferred(expectedEventDetailResponse)

        whenever(eventsRemoteDataSourceMock.getEventDetails(EVENT_ID))
            .thenReturn(expectedDeferredEvent)

        // ACT

        val eventDetailResponse = runBlocking {
            eventsRepository.getEventDetails(EVENT_ID).await()
        }

        // ASSERT

        assertEquals(expectedEventDetailResponse, eventDetailResponse)
    }

    @Test fun `Check in, when it is requested, then return deferred operation status`() {

        // ARRANGE

        val EVENT_ID = "1"
        val NAME = "Nome Sobrenome"
        val EMAIL = "email@sicredi.com.br"

        val expectedSuccessfulResponse = Response.success<Void>(null)

        val expectedDeferredSuccessfulResponse = CompletableDeferred(expectedSuccessfulResponse)

        whenever(eventsRemoteDataSourceMock.checkIn(EVENT_ID, NAME, EMAIL))
            .thenReturn(expectedDeferredSuccessfulResponse)

        // ACT

        val successfulResponse = runBlocking {
            eventsRepository.checkIn(EVENT_ID, NAME, EMAIL).await()
        }

        // ASSERT

        assertEquals(expectedSuccessfulResponse, successfulResponse)
    }
}