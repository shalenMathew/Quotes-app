package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.Quote

interface WidgetRepository {
    suspend fun updateWidget(quote: Quote): Result<Unit>
    suspend fun updateWidgetIfSameOrEmpty(quote: Quote): Result<Unit>
}