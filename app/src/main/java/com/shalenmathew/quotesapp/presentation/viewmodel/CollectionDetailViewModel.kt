package com.shalenmathew.quotesapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.domain.model.CollectionType
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetIfSameOrEmptyUseCase
import com.shalenmathew.quotesapp.presentation.screens.library.util.CollectionDetailEvent
import com.shalenmathew.quotesapp.presentation.screens.library.util.CollectionDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val collectionUseCases: CollectionUseCases,
    private val favQuoteUseCase: FavQuoteUseCase,
    private val customQuoteUseCases: CustomQuoteUseCases,
    private val updateWidgetIfSameOrEmptyUseCase: UpdateWidgetIfSameOrEmptyUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CollectionDetailState())
    val state = _state

    private var quotesJob: Job? = null

    fun onEvent(event: CollectionDetailEvent) {
        when (event) {
            is CollectionDetailEvent.LoadCollection -> {
                _state.value = _state.value.copy(
                    collectionId = event.id,
                    collectionType = CollectionType.fromId(event.id)
                )
                getQuotes()
                loadCollectionName(event.id)
            }
            is CollectionDetailEvent.OnSearchQueryChanged -> {
                _state.value = _state.value.copy(query = event.query)
                getQuotes()
            }
            is CollectionDetailEvent.Like -> {
                viewModelScope.launch {
                    val updatedQuote = favQuoteUseCase.favLikedQuote.saveLikedQuote(event.quote)
                    updateWidgetIfSameOrEmptyUseCase(updatedQuote)
                        .onFailure { Log.w("CollectionDetailVM", "Widget update failed: ${it.message}") }
                    getQuotes()
                }
            }
            is CollectionDetailEvent.Delete -> {
                viewModelScope.launch {
                    if (_state.value.collectionType is CollectionType.Custom) {
                        val customQuote = CustomQuote(
                            id = event.quote.id ?: return@launch,
                            quote = event.quote.quote,
                            author = event.quote.author,
                            createdAt = event.quote.updatedAt
                        )
                        customQuoteUseCases.deleteCustomQuote(customQuote)
                    } else {
                        collectionUseCases.removeQuoteFromCollection(
                            _state.value.collectionId,
                            event.quote.id ?: return@launch,
                            event.quote.isCustom
                        )
                    }
                    getQuotes()
                }
            }
        }
    }

    private fun getQuotes() {
        quotesJob?.cancel()
        val currentState = _state.value
        quotesJob = collectionUseCases.searchQuotesInCollection(currentState.collectionId, currentState.query)
            .onEach { quotes ->
                _state.value = _state.value.copy(quotes = quotes)
            }.launchIn(viewModelScope)
    }

    private fun loadCollectionName(id: Int) {
        viewModelScope.launch {
            val name = when (val type = CollectionType.fromId(id)) {
                CollectionType.Favorites -> "Favorites"
                CollectionType.Custom -> "Custom"
                is CollectionType.UserDefined -> collectionUseCases.getCollectionById(type.id)?.name ?: "Collection"
            }
            _state.value = _state.value.copy(collectionName = name)
        }
    }
}
