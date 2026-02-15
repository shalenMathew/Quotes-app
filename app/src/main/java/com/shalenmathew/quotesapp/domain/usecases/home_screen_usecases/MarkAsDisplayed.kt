package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import javax.inject.Inject

class MarkAsDisplayed @Inject constructor(private val quoteRepository: QuoteRepository) {
    suspend operator fun invoke(quoteId: Int) {
        quoteRepository.markAsDisplayed(quoteId)
    }
}
