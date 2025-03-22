package com.example.quotesapp.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.example.quotesapp.presentation.home_screen.util.QuoteEvent
import com.example.quotesapp.presentation.home_screen.util.QuoteState
import com.example.quotesapp.util.Resource
import com.example.quotesapp.util.saveQuote
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteUseCase: QuoteUseCase,
    @ApplicationContext private  val context: Context
):ViewModel()
{

    private val _quoteState = mutableStateOf(QuoteState())
    val quoteState = _quoteState

    init {
        getQuote()
    }

//    private fun  saveQuotesToPref(){
//        viewModelScope.launch {
//            context.saveQuote()
//        }
//    }

   private fun getQuote(){

       viewModelScope.launch {

           quoteUseCase.getQuote().collect{it->

               when(it){

                  is Resource.Success->{
//                      Log.d("TAG","viewmodel " + it.data!!.quotesList[0].toString())

                      it.data?.let { data->
                          // saving in state
                          _quoteState.value = _quoteState.value.copy(dataList = data.quotesList.toMutableList(),
                              qot = data.quotesOfTheDay[0], isLoading = false)

                          // saving the quote in datastore
                          context.saveQuote(data.quotesOfTheDay[0].quote)

                      } ?:{
                          _quoteState.value = _quoteState.value.copy(dataList = mutableListOf(),
                          qot =null , isLoading = false)
                      }
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
        }

    }


}