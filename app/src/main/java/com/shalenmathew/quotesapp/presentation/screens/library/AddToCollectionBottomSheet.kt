package com.shalenmathew.quotesapp.presentation.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.library.util.LibraryEvent
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCollectionBottomSheet(
    quote: Quote,
    onDismiss: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(quote.id) {
        viewModel.onEvent(LibraryEvent.SetSelectedQuote(quote.id, false))
    }

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onEvent(LibraryEvent.SetSelectedQuote(null, false))
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Add to Collection",
                fontSize = 22.sp,
                fontFamily = GIFont,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (state.collections.isEmpty()) {
                Text(
                    text = "No custom collections yet.\nCreate one in the Library!",
                    color = Color.Gray,
                    fontFamily = GIFont,
                    modifier = Modifier.padding(vertical = 24.dp),
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    items(state.collections) { collection ->
                        val isSelected = state.selectedQuoteCollectionIds.contains(collection.id)
                        CollectionSelectItem(
                            collection = collection,
                            isSelected = isSelected,
                            onSelect = {
                                if (isSelected) {
                                    viewModel.onEvent(LibraryEvent.RemoveQuoteFromCollection(collection.id, quote.id ?: return@CollectionSelectItem, false))
                                } else {
                                    viewModel.onEvent(LibraryEvent.AddQuoteToCollection(collection.id, quote.id ?: return@CollectionSelectItem, false))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionSelectItem(
    collection: Collection,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(if (isSelected) Color.White.copy(alpha = 0.1f) else customGrey2)
            .clickable { onSelect() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = collection.name,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = GIFont
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
