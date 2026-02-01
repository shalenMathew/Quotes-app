package com.shalenmathew.quotesapp.domain.usecases.widget

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.WidgetRepository
import javax.inject.Inject

class UpdateWidgetIfSameOrEmptyUseCase @Inject constructor(
    private val widgetRepository: WidgetRepository
) {
    suspend operator fun invoke(quote: Quote): Result<Unit> {
        return widgetRepository.updateWidgetIfSameOrEmpty(quote)
    }
}