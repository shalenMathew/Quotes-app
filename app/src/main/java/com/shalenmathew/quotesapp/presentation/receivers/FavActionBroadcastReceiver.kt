package com.shalenmathew.quotesapp.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.util.createOrUpdateNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavActionBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var quoteUseCase: QuoteUseCase

    lateinit var quote: Quote
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    override fun onReceive(context: Context?, intent: Intent?) {
        scope.launch {
            intent?.let {
                it.apply {
                    if (action == "ACTION_FAV") {
                        quote = Gson().fromJson(
                            getStringExtra("quote"),
                            Quote::class.java
                        )
                        val updatedQuote = quoteUseCase.likedQuote.invoke(quote)
                        context?.createOrUpdateNotification(updatedQuote)
                    }
                }
            }
        }
    }
}