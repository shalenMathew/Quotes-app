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
import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.CollectionQuoteCrossRef
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
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
    private val customQuoteUseCases: CustomQuoteUseCases,
    private val collectionUseCases: CollectionUseCases
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
                val collections = collectionUseCases.getAllCollections().first()
                val crossRefs = collectionUseCases.getAllCrossRefs()

                val backupData = BackupData(
                    likedQuotes = likedQuotes,
                    customQuotes = customQuotes,
                    collections = collections,
                    crossRefs = crossRefs,
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

                // Map old collection IDs to new ones
                val oldToNewIdMap = mutableMapOf<Int, Int>()

                backupData.collections.forEach { collection ->
                    val existing = collectionUseCases.getCollectionByName(collection.name)
                    if (existing != null) {
                        oldToNewIdMap[collection.id] = existing.id
                    } else {
                        val newId = collectionUseCases.addCollection(collection.name).fold(
                            onSuccess = {
                                collectionUseCases.getCollectionByName(collection.name)?.id
                            },
                            onFailure = { null }
                        )
                        if (newId != null) {
                            oldToNewIdMap[collection.id] = newId
                        }
                    }
                }

                backupData.crossRefs.forEach { crossRef ->
                    val newCollectionId = oldToNewIdMap[crossRef.collectionId]
                    if (newCollectionId != null) {
                        collectionUseCases.addQuoteToCollection(
                            newCollectionId,
                            crossRef.quoteId,
                            crossRef.isCustom
                        )
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
}