package com.shalenmathew.quotesapp.presentation.viewmodel

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.QuoteHome
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLatestQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLikedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetUndisplayedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.LikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.MarkAsDisplayed
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetIfSameOrEmptyUseCase
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteEvent
import com.shalenmathew.quotesapp.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
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
            getQuote, likedQuote, getLikedQuotes, getLatestQuote, markAsDisplayed, getUndisplayedQuotes
        )


    }

    @After
    fun tearDown() {

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuote_On_Success_Should_Update_State()= runTest{

        val testQuote = Quote(id=1, quote = "test",author="tester",liked = false, updatedAt = 1000L)
        val testQuoteOfTheDay = Quote(id=2, quote = "test2",author="tester2",liked = false, updatedAt = 2000L)

        val testHome = QuoteHome(listOf(testQuote),listOf(testQuoteOfTheDay))

        whenever(getQuote.invoke()).thenReturn(flowOf(Resource.Success(testHome)))
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))


        quotesVm = QuoteViewModel(quoteUseCase =quoteUseCase ,updateWidgetIfSameOrEmptyUseCase=updateWidgetUsecase)

        advanceUntilIdle()

        val state = quotesVm.quoteState.value

        assertEquals(false, state.isLoading)
        assertEquals(1,state.dataList.size)
        assertEquals("test",state.dataList[0].quote)

    }


}