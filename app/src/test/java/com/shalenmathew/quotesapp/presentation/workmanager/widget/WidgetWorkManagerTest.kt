package com.shalenmathew.quotesapp.presentation.workmanager.widget

import android.content.Context
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.GetCustomQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLikedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.SaveLikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WidgetWorkManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var workerParams: WorkerParameters

    @Mock
    private lateinit var getLikedQuotes: GetLikedQuotes

    @Mock
    private lateinit var saveLikedQuote: SaveLikedQuote

    @Mock
    private lateinit var getCustomQuotes: GetCustomQuotes

    @Mock
    private lateinit var scheduleWidgetRefresh: ScheduleWidgetRefresh

    @Mock
    private lateinit var updateWidgetUseCase: UpdateWidgetUseCase

    private lateinit var quoteUseCase: QuoteUseCase
    private lateinit var customQuoteUseCases: CustomQuoteUseCases
    private lateinit var worker: WidgetWorkManager

    @Before
    fun setUp() {
        // Construct real data classes with mocked individual use cases
        quoteUseCase = QuoteUseCase(
            getQuote = mock(),
            likedQuote = mock(),
            saveLikedQuote = saveLikedQuote,
            getLikedQuotes = getLikedQuotes,
            getLatestQuote = mock(),
            markAsDisplayed = mock(),
            getUndisplayedQuotes = mock()
        )

        customQuoteUseCases = CustomQuoteUseCases(
            getCustomQuotes = getCustomQuotes,
            saveCustomQuote = mock(),
            deleteCustomQuote = mock(),
            updateCustomQuote = mock()
        )

        worker = WidgetWorkManager(
            context,
            workerParams,
            quoteUseCase,
            customQuoteUseCases,
            scheduleWidgetRefresh,
            updateWidgetUseCase
        )
    }

    @Test
    fun `getRandomLikedQuote returns a quote when list is not empty`() = runTest {
        val quotes = listOf(
            Quote(id = 1, quote = "Q1", author = "A1", liked = true),
            Quote(id = 2, quote = "Q2", author = "A2", liked = true)
        )
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(quotes))

        val result = worker.getRandomLikedQuote()

        assertNotNull(result)
        assertTrue(quotes.contains(result))
    }

    @Test
    fun `getRandomLikedQuote returns null when list is empty`() = runTest {
        whenever(getLikedQuotes.invoke()).thenReturn(flowOf(emptyList()))

        val result = worker.getRandomLikedQuote()

        assertNull(result)
    }

    @Test
    fun `getRandomCustomQuote returns a quote mapped correctly`() = runTest {
        val customQuotes = listOf(
            CustomQuote(id = 1, quote = "CQ1", author = "CA1")
        )
        whenever(getCustomQuotes.invoke(any())).thenReturn(flowOf(customQuotes))

        val result = worker.getRandomCustomQuote()

        assertNotNull(result)
        result?.let {
            assertEquals(1, it.id)
            assertEquals("CQ1", it.quote)
            assertEquals("CA1", it.author)
            assertEquals(false, it.liked)
        }
    }

    @Test
    fun `getRandomCustomQuote returns null when list is empty`() = runTest {
        whenever(getCustomQuotes.invoke(any())).thenReturn(flowOf(emptyList()))

        val result = worker.getRandomCustomQuote()

        assertNull(result)
    }
}
