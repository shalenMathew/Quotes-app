package com.example.quotesapp.presentation.workmanager

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.example.quotesapp.presentation.widget.QuotesWidgetObj
import com.example.quotesapp.util.QUOTE_KEY
import com.example.quotesapp.util.Resource
import com.example.quotesapp.util.dataStore
import com.example.quotesapp.util.saveQuote
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull


@HiltWorker
class WidgetWorkManager @AssistedInject constructor(
    @Assisted private val  context: Context,
   @Assisted private val params: WorkerParameters,
    private val quoteUseCase: QuoteUseCase
) : CoroutineWorker(context, params)
{

    override suspend fun doWork(): Result {

      return  try {

          Log.d("WorkManagerStatus", "Work started")

          val response = fetchQuotes(context)

          if (!response){
              return Result.retry()
          }

          val savedQuote = context.dataStore.data.first()[QUOTE_KEY] ?: "No quote saved yet!!!"
          Log.d("WorkManagerStatus", "Saved Quote in DataStore work manager: $savedQuote")

          val glanceAppWidgetManager = GlanceAppWidgetManager(applicationContext)
          val widgetIds = glanceAppWidgetManager.getGlanceIds(QuotesWidgetObj::class.java)

          widgetIds.forEach { it->
              QuotesWidgetObj.updateAll(applicationContext)
          }

          return Result.success()

        }
      catch (e: Exception) {
          Log.d("WorkManagerStatus", "Exception in doWork", e)
            Result.failure()
        }

    }


    private suspend fun fetchQuotes(context: Context):Boolean {

   return try {

       Log.d("WorkManagerStatus", "inside fetchQuotes")

     val response =
         withTimeoutOrNull(5000) {
         quoteUseCase.getQuote()
             .filter { it is Resource.Success || it is Resource.Error}
             .first()
     }
        when(response){

            is Resource.Success->{
                val quote =  response.data?.quotesList?.getOrNull(0)?.quote

                if(quote!=null){
                    Log.d("WorkManagerStatus", "Fetched Quote: $quote")
                    context.saveQuote(quote)
                     true
                }else{
                    Log.d("WorkManagerStatus", "Quote is null")
                    false
                }

            }
            is Resource.Error-> {
                Log.d("WorkManagerStatus", "Error from fetchQuotes: ${response.message}")
                false
            }
            else -> {
                Log.d("WorkManagerStatus", "No response from fetchQuotes")
                false
            }
        }

    } catch (e: Exception){
        Log.d("WorkManagerStatus", "Exception in fetchQuotes", e)
      false
    }

}

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): WidgetWorkManager
    }

}