package com.shalenmathew.quotesapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetIfSameOrEmptyUseCase
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.FavQuoteEvent
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.FavQuoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavQuoteViewModel @Inject constructor(
    private val favQuoteUseCase: FavQuoteUseCase,
    private val updateWidgetIfSameOrEmptyUseCase: UpdateWidgetIfSameOrEmptyUseCase
) :
    ViewModel() {

    companion object {
        private const val TAG = "FavQuoteViewModel"
    }

    private val _favQuoteState = mutableStateOf(FavQuoteState())
    val favQuoteState = _favQuoteState

    init {
        getFavQuote()
    }

    private fun getFavQuote(query: String = _favQuoteState.value.query) {

        _favQuoteState.value = _favQuoteState.value.copy(isLoading = true)

        viewModelScope.launch {
            favQuoteUseCase.getFavQuote(query).collect {
                _favQuoteState.value = _favQuoteState.value.copy(dataList = it, isLoading = false)
            }
        }
    }


    fun onEvent(quoteEvent: FavQuoteEvent) {

        when (quoteEvent) {

            is FavQuoteEvent.Like -> {
                viewModelScope.launch {

                    val updatedQuote = favQuoteUseCase.favLikedQuote(quoteEvent.quote)
                    updateWidgetIfSameOrEmptyUseCase(updatedQuote)
                        .onFailure { Log.w(TAG, "Widget update failed: ${it.message}") }

                    _favQuoteState.value =
                        _favQuoteState.value.copy(dataList = _favQuoteState.value.dataList.map { quote ->
                            if (quote.id == updatedQuote.id) updatedQuote else quote
                        }.toMutableList(), isLoading = true)

                    delay(100)
                    _favQuoteState.value = _favQuoteState.value.copy(isLoading = false)

                }

            }

            is FavQuoteEvent.onSearchQueryChanged -> {
                _favQuoteState.value = _favQuoteState.value.copy(query = quoteEvent.query)
                getFavQuote()
            }

            is FavQuoteEvent.onRefresh -> {

//                job?.cancel()
//
//                job=viewModelScope.launch {
//                    delay(3000)
//                    getFavQuote()
//                }

            }

        }

    }


}
