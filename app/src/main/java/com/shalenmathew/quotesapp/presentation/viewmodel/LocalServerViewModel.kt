package com.shalenmathew.quotesapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.shalenmathew.quotesapp.data.local_server.LocalQuoteWebServer
import com.shalenmathew.quotesapp.data.local_server.LocalServerStatus
import com.shalenmathew.quotesapp.data.local_server.LocalWebServerService
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocalServerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localQuoteWebServer: LocalQuoteWebServer,
    quoteUseCase: QuoteUseCase,
    customQuoteUseCases: CustomQuoteUseCases
) : ViewModel() {

    val serverStatus: StateFlow<LocalServerStatus> = localQuoteWebServer.status

    val customQuotesCount: Flow<Int> = customQuoteUseCases.getCustomQuotes.getCount()
    val likedQuotesCount: Flow<Int> = quoteUseCase.getLikedQuotes.getCount()

    fun toggleServer(enable: Boolean) {
        if (enable) {
            LocalWebServerService.start(context)
        } else {
            LocalWebServerService.stop(context)
        }
    }

    fun refreshNetworkAddress() {
        localQuoteWebServer.refreshIpAddress()
    }
}
