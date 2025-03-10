package com.example.quotesapp.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.example.quotesapp.ui.fav_screen.util.FavQuoteEvent
import com.example.quotesapp.ui.fav_screen.util.FavQuoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavQuoteViewModel@Inject constructor(private val favQuoteUseCase: FavQuoteUseCase): ViewModel() {


    private val _favQuoteState = mutableStateOf(FavQuoteState())
    val favQuoteState = _favQuoteState


    init {
        getFavQuote()
    }

    private fun getFavQuote(){

        _favQuoteState.value=_favQuoteState.value.copy(isLoading = true)

        viewModelScope.launch {
            favQuoteUseCase.getFavQuote().collect(){it->
                _favQuoteState.value=_favQuoteState.value.copy(dataList = it, isLoading = false)
            }
        }
    }


    fun onEvent(quoteEvent: FavQuoteEvent){

        when(quoteEvent){

            is FavQuoteEvent.Like -> {
                viewModelScope.launch {

                    val updatedQuote = favQuoteUseCase.favLikedQuote(quoteEvent.quote)

                    _favQuoteState.value=_favQuoteState.value.copy(dataList = _favQuoteState.value.dataList.map { quote->
                        if(quote.id==updatedQuote.id) updatedQuote else quote
                    }.toMutableList(), isLoading = true)

                    delay(100)
                    _favQuoteState.value=_favQuoteState.value.copy(isLoading = false)

                }

            }
        }

    }


}