package br.com.natanaelribeiro.events.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.natanaelribeiro.basefeature.interfaces.CommandProvider
import br.com.natanaelribeiro.basefeature.models.GenericCommand
import br.com.natanaelribeiro.basefeature.viewmodels.SingleLiveEvent
import br.com.natanaelribeiro.eventsdata.contract.repositories.EventsRepository
import br.com.natanaelribeiro.eventsdata.models.Coupon
import br.com.natanaelribeiro.eventsdata.models.Event
import br.com.natanaelribeiro.eventsdata.models.Person
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class EventDetailsViewModelTest {

    private lateinit var eventsRepositoryMock: EventsRepository
    private lateinit var commandProviderMock: CommandProvider

    private lateinit var viewStateObserver: Observer<EventDetailsViewModel.ViewState>
    private lateinit var commandMock: SingleLiveEvent<GenericCommand>

    @JvmField @Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var eventDetailsViewModel: EventDetailsViewModel

    @Before
    fun setUp() {

        eventsRepositoryMock = mock()
        commandProviderMock = mock()

        viewStateObserver = mock()
        commandMock = mock()

        whenever(commandProviderMock.getCommand()).thenReturn(commandMock)

        eventDetailsViewModel = EventDetailsViewModel(
            eventsRepositoryMock,
            Dispatchers.Unconfined,
            commandProviderMock
        )
    }

    @Test fun `Get event details, when it is passed a valid eventId, then ShowEventDetails command is triggered`() {

        // ARRANGE

        val VALID_EVENT_ID = "1"

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

        val expectedEvent = Event(
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

        val expectedViewState = EventDetailsViewModel.ViewState()

        eventDetailsViewModel.viewState.observeForever(viewStateObserver)

        val EXPECTED_SHOW_EVENT_DETAILS_COMMAND = EventDetailsViewModel.Command.ShowEventDetails(expectedEvent)

        val expectedEventResponse = Response.success(expectedEvent)

        val expectedDeferredEvent = CompletableDeferred(expectedEventResponse)

        whenever(eventsRepositoryMock.getEventDetails(VALID_EVENT_ID))
            .thenReturn(expectedDeferredEvent)

        // ACT

        eventDetailsViewModel.getEventDetails(VALID_EVENT_ID)

        // ASSERT

        verify(viewStateObserver, times(1))
            .onChanged(expectedViewState.copy(isLoadingEvent = true))

        verify(viewStateObserver, times(2))
            .onChanged(expectedViewState.copy(isLoadingEvent = false))

        val commandCaptor = argumentCaptor<GenericCommand>()

        verify(commandMock, times(1)).postValue(commandCaptor.capture())

        val triggeredCommand = commandCaptor.firstValue
                as EventDetailsViewModel.Command.ShowEventDetails

        assertEquals(EXPECTED_SHOW_EVENT_DETAILS_COMMAND.event, triggeredCommand.event)
    }

    @Test fun `Get event details, when it is passed an invalid eventId, then ShowErrorLoadingEvent command is triggered`() {

        // ARRANGE

        val INVALID_EVENT_ID = "-1"

        val EXPECTED_REST_CODE = 400

        val expectedViewState = EventDetailsViewModel.ViewState()

        eventDetailsViewModel.viewState.observeForever(viewStateObserver)

        val EXPECTED_SHOW_ERROR_LOADING_EVENT_COMMAND
                = EventDetailsViewModel.Command.ShowErrorLoadingEvent

        val expectedEventResponse = Response.error<Event>(
            EXPECTED_REST_CODE,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ""
            )
        )

        val expectedDeferredEvent = CompletableDeferred(expectedEventResponse)

        whenever(eventsRepositoryMock.getEventDetails(INVALID_EVENT_ID))
            .thenReturn(expectedDeferredEvent)

        // ACT

        eventDetailsViewModel.getEventDetails(INVALID_EVENT_ID)

        // ASSERT

        verify(viewStateObserver, times(1))
            .onChanged(expectedViewState.copy(isLoadingEvent = true))

        verify(viewStateObserver, times(2))
            .onChanged(expectedViewState.copy(isLoadingEvent = false))


        verify(commandMock, times(1)).postValue(EXPECTED_SHOW_ERROR_LOADING_EVENT_COMMAND)
    }

    @Test fun `Check in, when it is passed a valid eventId, then ShowSuccessCheckInMessage command is triggered`() {

        // ARRANGE

        val VALID_EVENT_ID = "1"
        val NAME = "Nome Sobrenome"
        val EMAIL = "email@sicredi.com.br"

        val expectedViewState = EventDetailsViewModel.ViewState()

        eventDetailsViewModel.viewState.observeForever(viewStateObserver)

        val EXPECTED_SHOW_SUCCESS_CHECKIN_MESSAGE_COMMAND
                = EventDetailsViewModel.Command.ShowSuccessCheckInMessage

        val expectedSuccessfulResponse = Response.success<Void>(null)

        val expectedDeferredSuccessfulResponse = CompletableDeferred(expectedSuccessfulResponse)

        whenever(eventsRepositoryMock.checkIn(VALID_EVENT_ID, NAME, EMAIL))
            .thenReturn(expectedDeferredSuccessfulResponse)

        // ACT

        eventDetailsViewModel.checkIn(VALID_EVENT_ID, NAME, EMAIL)

        // ASSERT

        verify(viewStateObserver, times(1))
            .onChanged(expectedViewState.copy(isDoingCheckIn = true))

        verify(viewStateObserver, times(2))
            .onChanged(expectedViewState.copy(isDoingCheckIn = false))

        verify(commandMock, times(1)).postValue(EXPECTED_SHOW_SUCCESS_CHECKIN_MESSAGE_COMMAND)
    }

    @Test fun `Check in, when it is passed an invalid eventId, then ShowErrorDoingCheckIn command is triggered`() {

        // ARRANGE

        val INVALID_EVENT_ID = "1"
        val NAME = "Nome Sobrenome"
        val EMAIL = "email@sicredi.com.br"

        val EXPECTED_REST_CODE = 400

        val expectedViewState = EventDetailsViewModel.ViewState()

        eventDetailsViewModel.viewState.observeForever(viewStateObserver)

        val EXPECTED_SHOW_ERROR_DOING_CHECKIN_COMMAND
                = EventDetailsViewModel.Command.ShowErrorDoingCheckIn

        val expectedErrorResponse = Response.error<Void>(
            EXPECTED_REST_CODE,
            ResponseBody.create(
                MediaType.parse("application/json"),
                ""
            )
        )

        val expectedDeferredErrorResponse = CompletableDeferred(expectedErrorResponse)

        whenever(eventsRepositoryMock.checkIn(INVALID_EVENT_ID, NAME, EMAIL))
            .thenReturn(expectedDeferredErrorResponse)

        // ACT

        eventDetailsViewModel.checkIn(INVALID_EVENT_ID, NAME, EMAIL)

        // ASSERT

        verify(viewStateObserver, times(1))
            .onChanged(expectedViewState.copy(isDoingCheckIn = true))

        verify(viewStateObserver, times(2))
            .onChanged(expectedViewState.copy(isDoingCheckIn = false))

        verify(commandMock, times(1)).postValue(EXPECTED_SHOW_ERROR_DOING_CHECKIN_COMMAND)
    }
}