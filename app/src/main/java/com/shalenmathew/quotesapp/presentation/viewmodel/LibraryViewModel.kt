package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
import com.shalenmathew.quotesapp.presentation.screens.library.util.LibraryEvent
import com.shalenmathew.quotesapp.presentation.screens.library.util.LibraryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val collectionUseCases: CollectionUseCases
) : ViewModel() {

    private val _state = mutableStateOf(LibraryState())
    val state: State<LibraryState> = _state

    private var selectedQuoteJob: Job? = null

    init {
        getCollections()
    }

    private fun getCollections() {
        collectionUseCases.getAllCollections()
            .onEach { collections ->
                _state.value = _state.value.copy(
                    collections = collections
                )
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.AddCollection -> {
                viewModelScope.launch {
                    collectionUseCases.addCollection(event.name)
                        .onFailure { error ->
                            _state.value = _state.value.copy(error = error.message ?: "Failed to add collection")
                        }
                        .onSuccess {
                            _state.value = _state.value.copy(error = "")
                        }
                }
            }
            is LibraryEvent.UpdateCollection -> {
                viewModelScope.launch {
                    collectionUseCases.updateCollection(event.collection)
                        .onFailure { error ->
                            _state.value = _state.value.copy(error = error.message ?: "Failed to update collection")
                        }
                        .onSuccess {
                            _state.value = _state.value.copy(error = "")
                        }
                }
            }
            is LibraryEvent.DeleteCollection -> {
                viewModelScope.launch {
                    collectionUseCases.deleteCollection(event.collection)
                }
            }
            is LibraryEvent.AddQuoteToCollection -> {
                viewModelScope.launch {
                    collectionUseCases.addQuoteToCollection(event.collectionId, event.quoteId, event.isCustom)
                }
            }
            is LibraryEvent.RemoveQuoteFromCollection -> {
                viewModelScope.launch {
                    collectionUseCases.removeQuoteFromCollection(event.collectionId, event.quoteId, event.isCustom)
                }
            }
            is LibraryEvent.SetSelectedQuote -> {
                selectedQuoteJob?.cancel()
                if (event.quoteId != null) {
                    selectedQuoteJob = collectionUseCases.getCollectionIdsForQuote(event.quoteId, event.isCustom)
                        .onEach { ids ->
                            _state.value = _state.value.copy(selectedQuoteCollectionIds = ids)
                        }.launchIn(viewModelScope)
                } else {
                    _state.value = _state.value.copy(selectedQuoteCollectionIds = emptyList())
                }
            }
            is LibraryEvent.ClearError -> {
                _state.value = _state.value.copy(error = "")
            }
        }
    }
}
