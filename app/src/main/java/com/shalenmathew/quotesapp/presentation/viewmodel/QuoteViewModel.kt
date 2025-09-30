package com.shalenmathew.quotesapp.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.ObserveQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteEvent
import com.shalenmathew.quotesapp.presentation.screens.home_screen.util.QuoteState
import com.shalenmathew.quotesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteUseCase: QuoteUseCase,
    private val observeQuotes: ObserveQuotes,
    @ApplicationContext private  val context: Context
):ViewModel()
{

    private val _quoteState = mutableStateOf(QuoteState())
    val quoteState = _quoteState

    init {
        getQuote()
        observeDatabaseChanges()
    }

    private fun getQuote(){

       viewModelScope.launch {

           quoteUseCase.getQuote().collect{it->

               when(it){

                  is Resource.Success->{
                      Log.d("TAG","from viewmodel, fetched dat succesfully " + it.data?.quotesList[0].toString() )

                      it.data?.let { data->
                          // saving in state
                          _quoteState.value = _quoteState.value.copy(dataList = data.quotesList.toMutableList(),
                              qot = data.quotesOfTheDay[0], isLoading = false, error = "")

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

    private fun observeDatabaseChanges() {
        viewModelScope.launch {
            observeQuotes().collect { quotes ->
                // Update the dataList with the latest quotes from database
                // This ensures the UI stays synchronized when quotes are liked/unliked from other screens
                val currentState = _quoteState.value
                _quoteState.value = currentState.copy(dataList = quotes.toMutableList())
            }
        }
    }

    fun onEvent(quoteEvent: QuoteEvent){

        when(quoteEvent){

            is QuoteEvent.Like -> {
                viewModelScope.launch {
//                    quoteUseCase.likedQuote(quoteEvent.quote)
                    val updatedQuote = quoteUseCase.likedQuote(quoteEvent.quote)

                    _quoteState.value=_quoteState.value.copy(dataList = _quoteState.value.dataList.map { quote->
                        if(quote.id==updatedQuote.id) updatedQuote else quote
                    }.toMutableList(), isLoading = true)

                    delay(100)
                    _quoteState.value=_quoteState.value.copy(isLoading = false)

                }

            }
            is QuoteEvent.Retry -> {
                getQuote()
            }

        }

    }


}