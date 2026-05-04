package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.util.CustomQuoteEvent
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.util.CustomQuoteState
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomQuoteViewModel @Inject constructor(
    private val customQuoteUseCases: CustomQuoteUseCases,
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh
) : ViewModel() {

    private val _state = mutableStateOf(CustomQuoteState())
    val state = _state

    init {
        getCustomQuotes()
    }

    private fun getCustomQuotes(query: String = _state.value.query) {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            customQuoteUseCases.getCustomQuotes(query).collect { quotes ->
                _state.value = _state.value.copy(
                    customQuotes = quotes,
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: CustomQuoteEvent) {
        when (event) {
            is CustomQuoteEvent.SaveQuote -> {
                viewModelScope.launch {
                    val customQuote = CustomQuote(
                        quote = event.quote,
                        author = event.author.ifBlank { "Anonymous" }
                    )
                    customQuoteUseCases.saveCustomQuote(customQuote)
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                }
            }

            is CustomQuoteEvent.DeleteQuote -> {
                viewModelScope.launch {
                    customQuoteUseCases.deleteCustomQuote(event.quote)
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                }
            }

            is CustomQuoteEvent.OnSearchQueryChanged -> {
                _state.value = _state.value.copy(query = event.query)
                getCustomQuotes(event.query)
            }

            is CustomQuoteEvent.UpdateQuote -> {
                viewModelScope.launch {
                    customQuoteUseCases.updateCustomQuote(event.quote)
                    getCustomQuotes()
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                }
            }
        }
    }

//    fun getQuoteById(quoteId: Int): CustomQuote?{
//        return _state.value.customQuotes.find { it.id==quoteId }
//    }

}