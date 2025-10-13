package com.shalenmathew.quotesapp.presentation.workmanager.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.data.remote.dto.QuotesDtoItem
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetObj
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_KEY
import com.shalenmathew.quotesapp.util.dataStore
import com.shalenmathew.quotesapp.util.saveWidgetQuoteObject
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

          val savedQuote = context.dataStore.data.first()[WIDGET_QUOTE_KEY] ?: "No quote saved yet!!!"
          Log.d("WorkManagerStatus", "Saved Quote in DataStore work manager: $savedQuote")

          val glanceAppWidgetManager = GlanceAppWidgetManager(applicationContext)
          val widgetIds = glanceAppWidgetManager.getGlanceIds(QuotesWidgetObj::class.java)

          if(widgetIds.isNotEmpty()){
              widgetIds.forEach { it->
                  QuotesWidgetObj.updateAll(applicationContext)
              }
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
                 .filter { it is Resource.Success || it is Resource.Error }
                 .first()
         }
        when(response){

            is Resource.Success->{
                val dtoItem = response.data?.quotesList?.getOrNull(0) as? QuotesDtoItem

                if(dtoItem!=null){
                    Log.d("WorkManagerStatus", "Fetched Quote: ${dtoItem.q}")
                    // Create a Quote object with liked = false initially
                    // Use the hash field as id if available, otherwise generate one
                    val id = dtoItem.h?.toIntOrNull() ?: dtoItem.q.hashCode()
                    val quoteObject = Quote(
                        id = id,
                        quote = dtoItem.q,
                        author = dtoItem.a,
                        liked = false
                    )
                    context.saveWidgetQuoteObject(quoteObject)
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