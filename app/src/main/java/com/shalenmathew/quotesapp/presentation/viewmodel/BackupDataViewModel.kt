package com.shalenmathew.quotesapp.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.shalenmathew.quotesapp.BuildConfig
import com.shalenmathew.quotesapp.domain.model.BackupData
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject


@HiltViewModel
class BackupDataViewModel @Inject constructor(
    private val quoteUseCase: QuoteUseCase,
    private val customQuoteUseCases: CustomQuoteUseCases
): ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    suspend fun getLikedCount(): Int = quoteUseCase.getLikedQuotes().first().size
    suspend fun getCustomCount(): Int = customQuoteUseCases.getCustomQuotes("").first().size

    fun exportData(context: Context, uri: Uri, onComplete: (Boolean) -> Unit){
        isLoading = true
        viewModelScope.launch {
            try {

                val likedQuotes = quoteUseCase.getLikedQuotes().first()
                val customQuotes = customQuoteUseCases.getCustomQuotes("").first()

                val backupData = BackupData(
                    likedQuotes = likedQuotes,
                    customQuotes = customQuotes,
                    appVersionName = BuildConfig.VERSION_NAME
                )

                val jsonStr = Gson().toJson(backupData)

                withContext(Dispatchers.IO){
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(jsonStr.toByteArray())
                    }
                }
                isLoading = false
                onComplete(true)


            }catch (e: Exception){
                e.printStackTrace()
                isLoading = false
                onComplete(false)
            }
        }
    }

    fun importData(context: Context, uri: Uri, onComplete: (Boolean) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {

                val jsonStr = withContext(Dispatchers.IO){

                    val stringBuilder = StringBuilder()
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        BufferedReader(InputStreamReader(inputStream)).use { reader ->
                            var line: String? = reader.readLine()
                            while (line != null) {
                                stringBuilder.append(line)
                                line = reader.readLine()
                            }
                        }
                    }
                    stringBuilder.toString()

                }

                val backupData = Gson().fromJson(jsonStr, BackupData::class.java)

                backupData.likedQuotes.forEach { quote ->
                    //saving quote in db
                    quoteUseCase.saveLikedQuote(quote)
                }

                backupData.customQuotes.forEach { customQuote ->
                    customQuoteUseCases.saveCustomQuote(customQuote)
                }
                isLoading = false
                onComplete(true)


            }catch (e: Exception){
                e.printStackTrace()
                isLoading = false
                onComplete(false)
            }
        }
    }
}