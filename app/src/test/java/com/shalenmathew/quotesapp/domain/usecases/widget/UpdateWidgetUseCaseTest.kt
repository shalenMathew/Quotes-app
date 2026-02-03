package com.shalenmathew.quotesapp.domain.usecases.widget

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.WidgetRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class UpdateWidgetUseCaseTest {

    @Mock
    private lateinit var widgetRepository: WidgetRepository

    private lateinit var updateWidgetUseCase: UpdateWidgetUseCase

    @Before
    fun setUp() {
        updateWidgetUseCase = UpdateWidgetUseCase(widgetRepository)
    }

    @Test
    fun `should return success when repository updates widget successfully`() = runTest {
        val quote = Quote(1, "quote", "author", true)
        whenever(widgetRepository.updateWidget(quote)).thenReturn(Result.success(Unit))

        val result = updateWidgetUseCase(quote)

        verify(widgetRepository).updateWidget(quote)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return failure when repository fails to update widget`() = runTest {
        val quote = Quote(1, "quote", "author", true)
        val exception = RuntimeException("Update failed")
        whenever(widgetRepository.updateWidget(quote)).thenReturn(Result.failure(exception))

        val result = updateWidgetUseCase(quote)

        verify(widgetRepository).updateWidget(quote)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}