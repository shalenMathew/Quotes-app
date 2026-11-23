package com.shalenmathew.quotesapp.util

import org.junit.Assert.assertEquals
import org.junit.Test

class WidgetRefreshIntervalUtilTest {

    @Test
    fun `parseWidgetRefreshOptionToMinutes parses minute options accurately`() {
        assertEquals(5, parseWidgetRefreshOptionToMinutes("5 min"))
        assertEquals(10, parseWidgetRefreshOptionToMinutes("10 min"))
        assertEquals(15, parseWidgetRefreshOptionToMinutes("15 min"))
        assertEquals(30, parseWidgetRefreshOptionToMinutes("30 min"))
    }

    @Test
    fun `parseWidgetRefreshOptionToMinutes parses hour options accurately`() {
        assertEquals(60, parseWidgetRefreshOptionToMinutes("1 hr"))
        assertEquals(360, parseWidgetRefreshOptionToMinutes("6 hr"))
        assertEquals(720, parseWidgetRefreshOptionToMinutes("12 hr"))
        assertEquals(1440, parseWidgetRefreshOptionToMinutes("24 hr"))
    }

    @Test
    fun `formatWidgetRefreshMinutesToOption formats minutes and hours properly`() {
        assertEquals("5 min", formatWidgetRefreshMinutesToOption(5))
        assertEquals("10 min", formatWidgetRefreshMinutesToOption(10))
        assertEquals("15 min", formatWidgetRefreshMinutesToOption(15))
        assertEquals("30 min", formatWidgetRefreshMinutesToOption(30))
        assertEquals("1 hr", formatWidgetRefreshMinutesToOption(60))
        assertEquals("6 hr", formatWidgetRefreshMinutesToOption(360))
        assertEquals("12 hr", formatWidgetRefreshMinutesToOption(720))
        assertEquals("24 hr", formatWidgetRefreshMinutesToOption(1440))
    }
}
