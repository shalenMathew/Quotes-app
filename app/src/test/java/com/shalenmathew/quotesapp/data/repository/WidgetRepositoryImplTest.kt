package com.shalenmathew.quotesapp.data.repository

import androidx.glance.GlanceId
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.GlanceWidgetManager
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetObj
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class WidgetRepositoryImplTest {

    @Mock
    private lateinit var glanceWidgetManager: GlanceWidgetManager

    private lateinit var widgetRepository: WidgetRepositoryImpl

    private val testQuote = Quote(id = 1, quote = "Test quote", author = "Author", liked = true)

    @Before
    fun setUp() {
        widgetRepository = WidgetRepositoryImpl(glanceWidgetManager)
    }

    @Test
    fun `updateWidget should return failure when quote id is null`() = runTest {
        val result = widgetRepository.updateWidget(testQuote.copy(id = null))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        verifyNoInteractions(glanceWidgetManager)
    }

    @Test
    fun `updateWidget should return success when widget state is updated`() = runTest {
        val mockGlanceId = mock<GlanceId>()
        whenever(glanceWidgetManager.getWidgetIds(QuotesWidgetObj::class)).thenReturn(
            listOf(mockGlanceId)
        )

        val result = widgetRepository.updateWidget(testQuote)

        assertTrue(result.isSuccess)
        verify(glanceWidgetManager).updateWidgetState(eq(mockGlanceId), any())
        verify(glanceWidgetManager).updateAllWidgets(QuotesWidgetObj)
    }

    @Test
    fun `updateWidget should return failure when exception occurs`() = runTest {
        val mockGlanceId = mock<GlanceId>()
        val exception = RuntimeException("Widget update failed")
        whenever(glanceWidgetManager.getWidgetIds(QuotesWidgetObj::class)).thenReturn(
            listOf(mockGlanceId)
        )
        whenever(glanceWidgetManager.updateWidgetState(any(), any())).thenThrow(exception)

        val result = widgetRepository.updateWidget(testQuote)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `updateWidgetIfSameOrEmpty should return failure when quote id is null`() = runTest {
        val result = widgetRepository.updateWidgetIfSameOrEmpty(testQuote.copy(id = null))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        verifyNoInteractions(glanceWidgetManager)
    }

    @Test
    fun `updateWidgetIfSameOrEmpty should update when widget is empty`() = runTest {
        val mockGlanceId = mock<GlanceId>()
        whenever(glanceWidgetManager.getWidgetIds(QuotesWidgetObj::class)).thenReturn(
            listOf(mockGlanceId)
        )

        val result = widgetRepository.updateWidgetIfSameOrEmpty(testQuote)

        assertTrue(result.isSuccess)
        verify(glanceWidgetManager).updateWidgetState(eq(mockGlanceId), any())
        verify(glanceWidgetManager).updateAllWidgets(QuotesWidgetObj)
    }

    @Test
    fun `updateWidgetIfSameOrEmpty should return failure when exception occurs`() = runTest {
        val mockGlanceId = mock<GlanceId>()
        val exception = RuntimeException("Widget update failed")
        whenever(glanceWidgetManager.getWidgetIds(QuotesWidgetObj::class)).thenReturn(
            listOf(mockGlanceId)
        )
        whenever(glanceWidgetManager.updateWidgetState(any(), any())).thenThrow(exception)

        val result = widgetRepository.updateWidgetIfSameOrEmpty(testQuote)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}