package com.shalenmathew.quotesapp.domain.usecases.widget

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.WidgetRepository
import javax.inject.Inject

class UpdateWidgetUseCase @Inject constructor(
    private val widgetRepository: WidgetRepository
) {
    suspend operator fun invoke(quote: Quote): Result<Unit> {
        return widgetRepository.updateWidget(quote)
    }
}