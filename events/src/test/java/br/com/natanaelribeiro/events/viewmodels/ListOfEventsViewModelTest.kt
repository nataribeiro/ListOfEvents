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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class ListOfEventsViewModelTest {

    private lateinit var eventsRepositoryMock: EventsRepository
    private lateinit var commandProviderMock: CommandProvider

    private lateinit var viewStateObserver: Observer<ListOfEventsViewModel.ViewState>
    private lateinit var commandMock: SingleLiveEvent<GenericCommand>

    @JvmField @Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var listOfEventsViewModel: ListOfEventsViewModel

    @Before fun setUp() {

        eventsRepositoryMock = mock()
        commandProviderMock = mock()

        viewStateObserver = mock()
        commandMock = mock()

        whenever(commandProviderMock.getCommand()).thenReturn(commandMock)

        listOfEventsViewModel = ListOfEventsViewModel(
            eventsRepositoryMock,
            Dispatchers.Unconfined,
            commandProviderMock
        )
    }

    @Test fun `Get events, when it is requested, then ShowListOfEvents command is triggered`() {

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

        val expectedViewState = ListOfEventsViewModel.ViewState()

        listOfEventsViewModel.viewState.observeForever(viewStateObserver)

        val EXPECTED_SHOW_LIST_OF_EVENTS_COMMAND = ListOfEventsViewModel.Command.ShowListOfEvents(expectedListOfEvents)

        val expectedListOfEventsResponse = Response.success(expectedListOfEvents)

        val expectedDeferredListOfEvents = CompletableDeferred(expectedListOfEventsResponse)

        whenever(eventsRepositoryMock.getEvents())
            .thenReturn(expectedDeferredListOfEvents)

        // ACT

        listOfEventsViewModel.getEvents()

        // ASSERT

        verify(viewStateObserver, times(1))
            .onChanged(expectedViewState.copy(isLoadingEvents = true))

        verify(viewStateObserver, times(2))
            .onChanged(expectedViewState.copy(isLoadingEvents = false))

        val commandCaptor = argumentCaptor<GenericCommand>()

        verify(commandMock, times(1)).postValue(commandCaptor.capture())

        val triggeredCommand = commandCaptor.firstValue as ListOfEventsViewModel.Command.ShowListOfEvents

        assertEquals(EXPECTED_SHOW_LIST_OF_EVENTS_COMMAND.eventsList, triggeredCommand.eventsList)

    }

}