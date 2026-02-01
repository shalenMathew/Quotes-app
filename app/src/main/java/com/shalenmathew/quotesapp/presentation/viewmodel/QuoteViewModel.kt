package com.shalenmathew.quotesapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetIfSameOrEmptyUseCase
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteEvent
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteState
import com.shalenmathew.quotesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteUseCase: QuoteUseCase,
    private val updateWidgetIfSameOrEmptyUseCase: UpdateWidgetIfSameOrEmptyUseCase,
):ViewModel()
{
    companion object {
        private const val TAG = "QuoteViewModel"
    }

    private val _quoteState = mutableStateOf(QuoteState())
    val quoteState = _quoteState

    init {
        getQuote()
        observeLikedQuotes()
    }

    private fun getQuote(){

       viewModelScope.launch {

           quoteUseCase.getQuote().collect{it->

               when(it){

                  is Resource.Success->{
                      Log.d(TAG,"from viewmodel, fetched data successfully " + it.data?.quotesList[0].toString() )

                      it.data?.let { data->
                          // saving in state
                          _quoteState.value = _quoteState.value.copy(dataList = data.quotesList.toMutableList(),
                              qot = data.quotesOfTheDay[0], isLoading = false, error = "")

                          val firstQuote = data.quotesList.firstOrNull()
                          firstQuote?.let { quote ->
                              updateWidgetIfSameOrEmptyUseCase(quote)
                                  .onFailure { Log.w(TAG, "Widget update failed: ${it.message}") }
                          }

                      } ?:{
                          _quoteState.value = _quoteState.value.copy(dataList = mutableListOf(),
                          qot =null , isLoading = false, error = "")
                      }
                  }

                   is Resource.Error -> {
                     _quoteState.value = _quoteState.value.copy(error = it.message ?: "Something went wrong", isLoading = false)
                   }
                   is Resource.Loading -> {
                       _quoteState.value =_quoteState.value.copy(isLoading = true, dataList = mutableListOf(), error = "")
                   }
               }
           }
       }
    }

    private fun observeLikedQuotes() {
        viewModelScope.launch {
            quoteUseCase.getLikedQuotes().collect { likedQuotes ->
                // Update the like status of quotes in the current state
                _quoteState.value = _quoteState.value.copy(
                    dataList = _quoteState.value.dataList.map { quote ->
                        // Check if this quote is in the liked quotes list
                        val isLiked = likedQuotes.any { likedQuote -> likedQuote.id == quote.id }
                        quote.copy(liked = isLiked)
                    }.toMutableList()
                )
            }
        }
    }

    fun onEvent(quoteEvent: QuoteEvent){

        when(quoteEvent){

            is QuoteEvent.Like -> {
                viewModelScope.launch {
                    val updatedQuote = quoteUseCase.likedQuote(quoteEvent.quote)
                    updateWidgetIfSameOrEmptyUseCase(updatedQuote)
                        .onFailure { Log.w(TAG, "Widget update failed: ${it.message}") }

                    _quoteState.value=_quoteState.value.copy(dataList = _quoteState.value.dataList.map { quote->
                        if(quote.id==updatedQuote.id) updatedQuote else quote
                    }.toMutableList())
                }
            }
            is QuoteEvent.Swipe -> {
                // Move the swiped item to the back of the list
                val currentList = _quoteState.value.dataList.toList() // Convert to immutable list first
                val newList = currentList.filter { it.id != quoteEvent.quote.id } + quoteEvent.quote
                _quoteState.value=_quoteState.value.copy(dataList = newList.toMutableList())
            }
            is QuoteEvent.Retry -> {
                getQuote()
            }

        }

    }


}