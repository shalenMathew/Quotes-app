package com.shalenmathew.quotesapp.presentation.viewmodel

import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.DeleteCustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.GetCustomQuotes
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.SaveCustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.UpdateCustomQuote
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.util.CustomQuoteEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CustomQuoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getCustomQuotes: GetCustomQuotes
    @Mock
    private lateinit var saveCustomQuote: SaveCustomQuote
    @Mock
    private lateinit var deleteCustomQuote: DeleteCustomQuote
    @Mock
    private lateinit var updateCustomQuote: UpdateCustomQuote
    @Mock
    private lateinit var scheduleWidgetRefresh: ScheduleWidgetRefresh

    private lateinit var useCases: CustomQuoteUseCases
    private lateinit var viewModel: CustomQuoteViewModel

    @Before
    fun setUp() {
        useCases = CustomQuoteUseCases(
            getCustomQuotes, saveCustomQuote, deleteCustomQuote, updateCustomQuote
        )
    }


    @Test
    fun onEvent_SaveQuote_With_Blank_Author_Should_Save_As_Anonymous() = runTest {
        whenever(getCustomQuotes.invoke(any())).thenReturn(flowOf(emptyList()))
        viewModel = CustomQuoteViewModel(useCases, scheduleWidgetRefresh)
        advanceUntilIdle()

        // Act
        viewModel.onEvent(CustomQuoteEvent.SaveQuote(quote = "Hello", author = "   "))
        advanceUntilIdle()

        // Assert: Use check {} to verify the PROPERTIES of the object saved
        verify(saveCustomQuote).invoke(org.mockito.kotlin.check { savedQuote ->
            assertEquals("Hello", savedQuote.quote)
            assertEquals("Anonymous", savedQuote.author)
        })
    }

    @Test
    fun onEvent_OnSearchQueryChanged_Should_Update_Query_And_Refresh() = runTest {
        whenever(getCustomQuotes.invoke(any())).thenReturn(flowOf(emptyList()))
        viewModel = CustomQuoteViewModel(useCases, scheduleWidgetRefresh)
        advanceUntilIdle()

        // Act
        viewModel.onEvent(CustomQuoteEvent.OnSearchQueryChanged("Life"))
        advanceUntilIdle()

        // Assert
        assertEquals("Life", viewModel.state.value.query)
        // Verify getCustomQuotes was called with the new query
        verify(getCustomQuotes).invoke("Life")
    }

    @Test
    fun onEvent_DeleteQuote_Should_Call_UseCase() = runTest {
        whenever(getCustomQuotes.invoke(any())).thenReturn(flowOf(emptyList()))
        viewModel = CustomQuoteViewModel(useCases, scheduleWidgetRefresh)
        advanceUntilIdle()

        val testQuote = CustomQuote(id = 1, quote = "Delete me", author = "tester")

        // Act
        viewModel.onEvent(CustomQuoteEvent.DeleteQuote(testQuote))
        advanceUntilIdle()

        // Assert
        verify(deleteCustomQuote).invoke(testQuote)
    }

    @Test
    fun onEvent_UpdateQuote_Should_Call_UseCase_And_Refresh() = runTest {
        whenever(getCustomQuotes.invoke(any())).thenReturn(flowOf(emptyList()))
        viewModel = CustomQuoteViewModel(useCases, scheduleWidgetRefresh)
        advanceUntilIdle()

        val testQuote = CustomQuote(id = 1, quote = "Update me", author = "tester")

        // Act
        viewModel.onEvent(CustomQuoteEvent.UpdateQuote(testQuote))
        advanceUntilIdle()

        // Assert
        verify(updateCustomQuote).invoke(testQuote)
        // verify it refreshed (2 calls total: 1 for init, 1 for update)
        verify(getCustomQuotes, org.mockito.kotlin.times(2)).invoke(any())
    }
}
