package com.example.quotesapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.example.quotesapp.ui.home_screen.util.QuoteEvent
import com.example.quotesapp.ui.home_screen.util.QuoteState
import com.example.quotesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(private val quoteUseCase: QuoteUseCase):ViewModel() {

    private val _quoteState = mutableStateOf(QuoteState())
    val quoteState = _quoteState

    init {
        getQuote()
    }

   private fun getQuote(){

       viewModelScope.launch {

           quoteUseCase.getQuote().collect{it->

               when(it){
                  is Resource.Success->{
//                      Log.d("TAG","viewmodel " + it.data!!.quotesList[0].toString())

                      it.data?.let { data->
                          _quoteState.value = _quoteState.value.copy(dataList = data.quotesList.toMutableList(),
                              qot = data.quotesOfTheDay[0], isLoading = false)
                      } ?:{ _quoteState.value = _quoteState.value.copy(dataList = mutableListOf(),
                          qot =null , isLoading = false) }


                  }

                   is Resource.Error -> {
                     _quoteState.value = _quoteState.value.copy(error = it.message ?: "Something went wrong", isLoading = false)
                   }
                   is Resource.Loading -> {
                       _quoteState.value =_quoteState.value.copy(isLoading = true)
                   }
               }
           }
       }
    }

    fun onEvent(quoteEvent: QuoteEvent){

        when(quoteEvent){

            is QuoteEvent.Like -> {

                Log.d("TAG","Before true-"+quoteEvent.quote.liked.toString())
                Log.d("TAG","Before id-"+ quoteEvent.quote.id)
                viewModelScope.launch {
//                    quoteUseCase.likedQuote(quoteEvent.quote)
                    val updatedQuote = quoteUseCase.likedQuote(quoteEvent.quote)

                    _quoteState.value=_quoteState.value.copy(dataList = _quoteState.value.dataList.map { quote->
                        if(quote.id==updatedQuote.id) updatedQuote else quote
                    }.toMutableList(), isLoading = true)

                    delay(100)
                    _quoteState.value=_quoteState.value.copy(isLoading = false)

//                    Log.d("TAG","After liked-"+ updatedQuote.liked)
//                    Log.d("TAG","After-"+ updatedQuote.quote)
//                    Log.d("TAG","After id-"+ updatedQuote.id)
                }

            }
        }

    }


}