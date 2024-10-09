package com.example.quotesapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.example.quotesapp.domain.usecases.fav_screen_usecases.GetFavQuote
import com.example.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.example.quotesapp.ui.fav_screen.util.FavQuoteEvent
import com.example.quotesapp.ui.fav_screen.util.FavQuoteState
import com.example.quotesapp.ui.home_screen.util.QuoteEvent
import com.example.quotesapp.ui.home_screen.util.QuoteState
import com.example.quotesapp.util.Resource
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

        viewModelScope.launch {

            favQuoteUseCase.getFavQuote().collect(){it->

                when(it){
                    is Resource.Success->{
                        it.data?.let { data->
                            _favQuoteState.value = _favQuoteState.value.copy(dataList = data, isLoading = false)
                        } ?:{ _favQuoteState.value = _favQuoteState.value.copy(dataList = emptyList(), isLoading = false) }
                    }

                    is Resource.Error -> {
                        _favQuoteState.value = _favQuoteState.value.copy(error = it.message ?: "Something went wrong", isLoading = false)
                    }

                    is Resource.Loading -> {
                        _favQuoteState.value =_favQuoteState.value.copy(isLoading = true)
                    }

                }
            }
        }
    }


    fun onEvent(quoteEvent: FavQuoteEvent){

        when(quoteEvent){

            is FavQuoteEvent.Like -> {

                Log.d("TAG","Before liked-"+quoteEvent.quote.liked.toString())
                Log.d("TAG","Before id-"+ quoteEvent.quote.id)
                viewModelScope.launch {
//                    quoteUseCase.likedQuote(quoteEvent.quote)
                    val updatedQuote = favQuoteUseCase.favLikedQuote(quoteEvent.quote)

                    _favQuoteState.value=_favQuoteState.value.copy(dataList = _favQuoteState.value.dataList.map { quote->
                        if(quote.id==updatedQuote.id) updatedQuote else quote
                    }.toMutableList(), isLoading = true)

                    delay(100)
                    _favQuoteState.value=_favQuoteState.value.copy(isLoading = false)

//                    Log.d("TAG","After liked-"+ updatedQuote.liked)
//                    Log.d("TAG","After-"+ updatedQuote.quote)
//                    Log.d("TAG","After id-"+ updatedQuote.id)
                }

            }
        }

    }


}