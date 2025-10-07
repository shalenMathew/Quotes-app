package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ShareQuoteUiState(
    val defaultTheme: QuoteStyle,
    val displayStyle: QuoteStyle,
    val openStylePicker: Boolean = false
)

@HiltViewModel
class ShareQuoteViewModel @Inject constructor(
    private val defaultQuoteStylePreferences: DefaultQuoteStylePreferences
) : ViewModel() {
    private val _state = MutableStateFlow(
        ShareQuoteUiState(
            defaultTheme = defaultQuoteStylePreferences.getDefaultQuoteStyle(),
            displayStyle = defaultQuoteStylePreferences.getDefaultQuoteStyle(),
        )
    )
    val state: StateFlow<ShareQuoteUiState> = _state.asStateFlow()

    fun onAction(action: ShareQuoteUiAction) {
        when (action) {
            is ShareQuoteUiAction.UpdateDefaultStyle -> {
                _state.update {
                    it.copy(
                        defaultTheme = action.quoteStyle,
                        displayStyle = action.quoteStyle,
                    )
                }
                defaultQuoteStylePreferences.saveDefaultQuoteStyle(action.quoteStyle)
            }

            is ShareQuoteUiAction.UpdateDisplayStyle -> {
                _state.update {
                    it.copy(
                        displayStyle = action.quoteStyle,
                        openStylePicker = false,
                    )
                }
            }

            is ShareQuoteUiAction.ShowStylePicker -> {
                _state.update {
                    it.copy(
                        openStylePicker = true
                    )
                }
            }

            ShareQuoteUiAction.DismissPicker -> {
                _state.update {
                    it.copy(
                        openStylePicker = false
                    )
                }
            }
        }
    }
}

sealed interface ShareQuoteUiAction {
    data class UpdateDefaultStyle(val quoteStyle: QuoteStyle) : ShareQuoteUiAction
    data class UpdateDisplayStyle(val quoteStyle: QuoteStyle) : ShareQuoteUiAction
    data object ShowStylePicker : ShareQuoteUiAction
    data object DismissPicker : ShareQuoteUiAction

}