package com.shalenmathew.quotesapp.presentation.viewmodel

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.QuoteHome
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLatestQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLikedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetUndisplayedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.LikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.SaveLikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.MarkAsDisplayed
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetIfSameOrEmptyUseCase
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteEvent
import com.shalenmathew.quotesapp.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class QuoteViewModelTest {


    private lateinit var quoteUseCase: QuoteUseCase


    @Mock
    private lateinit var getQuote: GetQuote
    @Mock private lateinit var getLikedQuotes: GetLikedQuotes
    @Mock private lateinit var likedQuote: LikedQuote
    @Mock private lateinit var saveLikedQuote: SaveLikedQuote
    @Mock private lateinit var getLatestQuote: GetLatestQuote
    @Mock private lateinit var markAsDisplayed: MarkAsDisplayed
    @Mock private lateinit var getUndisplayedQuotes: GetUndisplayedQuotes
    @Mock private lateinit var updateWidgetUsecase: UpdateWidgetIfSameOrEmptyUseCase

    private lateinit var quotesVm:QuoteViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Before
    fun setUp() {

        quoteUseCase = QuoteUseCase(
            getQuote,
            likedQuote,
            saveLikedQuote,
            getLikedQuotes,
            getLatestQuote,
            markAsDisplayed,
            getUndisplayedQuotes,
            getRandomRemoteQuote = org.mockito.kotlin.mock()
        )


    }

    @After
    fun tearDown() {

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuote_On_Success_Should_Update_State()= runTest{

        val testQuote = Quote(id=1, quote = "test",author="tester",liked = false, updatedAt = 1000L)
        val testQuoteOfTheDay = Quote(id=2, quote = "Quote of the day",author="tester2",liked = false, updatedAt = 2000L)

        val testHome = QuoteHome(listOf(testQuote),listOf(testQuoteOfTheDay))

        whenever(getQuote.invoke()).thenReturn(flowOf(Resource.Success(testHome)))
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))


        quotesVm = QuoteViewModel(quoteUseCase =quoteUseCase ,updateWidgetIfSameOrEmptyUseCase=updateWidgetUsecase)

        advanceUntilIdle()

        val state = quotesVm.quoteState.value


        // if everything went fine then our data should be added in state
        assertEquals(false, state.isLoading)
        assertEquals(1,state.dataList.size)
        assertEquals("test",state.dataList[0].quote)

        assertEquals("Quote of the day",state.qot?.quote)

    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuote_On_Failure()= runTest{

        whenever(getQuote.invoke()).thenReturn(flowOf(Resource.Error("Network error")))
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))

        quotesVm = QuoteViewModel(quoteUseCase =quoteUseCase ,updateWidgetIfSameOrEmptyUseCase=updateWidgetUsecase)

        advanceUntilIdle()

        val state = quotesVm.quoteState.value

        assertEquals(false,state.isLoading)
        assertEquals("Network error",state.error)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuote_On_Loading()= runTest{

        whenever(getQuote.invoke()).thenReturn(flowOf(Resource.Loading(true)))
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))

        quotesVm = QuoteViewModel(quoteUseCase =quoteUseCase ,updateWidgetIfSameOrEmptyUseCase=updateWidgetUsecase)

        advanceUntilIdle()

        val state = quotesVm.quoteState.value

        assertEquals(true,state.isLoading)
        assertEquals("",state.error)
        assertEquals(emptyList(),state.dataList)
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onEvent_MarkAsDisplayed_Not_Fetching_New_Quotes() = runTest{

        val quote1 = Quote(id=1, quote = "first displayed",author="tester",liked = false, updatedAt = 1000L)
        val remainingQuote = Quote(id=2, quote = "undisplayed quote",author="tester2",liked = false, updatedAt = 1000L)
        val testQuoteOfTheDay = Quote(id=0, quote = "Qot",author="tester2",liked = false, updatedAt = 2000L)


        val testHome = QuoteHome(listOf(quote1,testQuoteOfTheDay),listOf(testQuoteOfTheDay))

        whenever(getQuote.invoke()).thenReturn(flowOf(Resource.Success(testHome)))
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))

        quotesVm = QuoteViewModel(quoteUseCase =quoteUseCase ,updateWidgetIfSameOrEmptyUseCase=updateWidgetUsecase)

        advanceUntilIdle()

        whenever(getUndisplayedQuotes.invoke()).thenReturn(Resource.Success(listOf(remainingQuote)))


        quotesVm.onEvent(QuoteEvent.MarkAsDisplayed(1)) // quote id 1 is displayed ....

        advanceUntilIdle()

        val state = quotesVm.quoteState.value

        assertEquals(1,state.dataList.size)
        assertEquals(2,state.dataList[0].id) // ... so it should return quote which is undisplayed
        assertEquals("undisplayed quote",state.dataList[0].quote)


        verify(getQuote, times(1)).invoke() // getQuote() will be called only 1 time (i.e on init ) , the new quotes wont
    // be fetched from network  until db is empty
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onEvent_MarkAsDisplayed_Fetching_New_Quotes() = runTest{

        val quote1 = Quote(id=1, quote = "first displayed",author="tester",liked = false, updatedAt = 1000L)
        val testQuoteOfTheDay = Quote(id=0, quote = "Qot",author="tester2",liked = false, updatedAt = 2000L)


        val testHome = QuoteHome(listOf(quote1),listOf(testQuoteOfTheDay))

        whenever(getQuote.invoke()).thenReturn(flowOf(Resource.Success(testHome)))
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))

        quotesVm = QuoteViewModel(quoteUseCase =quoteUseCase ,updateWidgetIfSameOrEmptyUseCase=updateWidgetUsecase)

        advanceUntilIdle()

        whenever(getUndisplayedQuotes.invoke()).thenReturn(Resource.Success(emptyList()))


        quotesVm.onEvent(QuoteEvent.MarkAsDisplayed(1))

        advanceUntilIdle()

        val state = quotesVm.quoteState.value

        assertEquals(1,state.dataList.size)

        assertEquals(1,state.dataList[0].id)

        assertEquals("first displayed",state.dataList[0].quote)

        verify(getQuote, times(2)).invoke() // getQuote() will be called only 1 time (i.e on init ) , the new quotes wont
        // be fetched from network  until db is empty
    }





}