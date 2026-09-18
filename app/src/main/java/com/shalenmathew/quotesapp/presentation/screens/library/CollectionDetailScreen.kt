package com.shalenmathew.quotesapp.presentation.screens.library

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.toQuote
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.util.DeleteConfirmationDialog
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.GlowingTriangle
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.RainbowRays
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.WhiteBeam
import com.shalenmathew.quotesapp.presentation.screens.library.util.CollectionDetailEvent
import com.shalenmathew.quotesapp.presentation.screens.library.util.CollectionDetailState
import com.shalenmathew.quotesapp.presentation.screens.library.util.LibraryQuoteItem
import com.shalenmathew.quotesapp.presentation.screens.library.util.WhiteCancelIcon
import com.shalenmathew.quotesapp.presentation.screens.library.util.animatedBorder
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.Grey
import com.shalenmathew.quotesapp.presentation.viewmodel.CollectionDetailViewModel
import com.shalenmathew.quotesapp.util.Constants
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    collectionId: Int,
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: CollectionDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val hapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(collectionId) {
        viewModel.onEvent(CollectionDetailEvent.LoadCollection(collectionId))
    }

    var clickedSearch by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (clickedSearch) 1f else 0f,
        label = "",
        animationSpec = tween(2000)
    )

    val pullRefreshState = rememberPullToRefreshState()
    val isRefreshing = state.isLoading

    var quoteToDelete by remember { mutableStateOf<Quote?>(null) }

    val cardOffset by animateIntAsState(
        targetValue = when {
            isRefreshing -> 250
            pullRefreshState.distanceFraction in 0f..1f -> (250 * pullRefreshState.distanceFraction).roundToInt()
            pullRefreshState.distanceFraction > 1f -> (250 + ((pullRefreshState.distanceFraction - 1f) * .1f) * 100).roundToInt()
            else -> 0
        },
        label = "cardOffset"
    )

    val cardRotation by animateFloatAsState(
        targetValue = when {
            isRefreshing || pullRefreshState.distanceFraction > 1f -> 5f
            pullRefreshState.distanceFraction > 0f -> 5 * pullRefreshState.distanceFraction
            else -> 0f
        },
        label = "cardRotation"
    )

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .pullToRefresh(
                isRefreshing = isRefreshing,
                onRefresh = { /* Trigger refresh */ },
                state = pullRefreshState
            )
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            Text(
                text = state.collectionName,
                fontSize = 35.sp,
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                color = White,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
            )

            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.onEvent(CollectionDetailEvent.OnSearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 15.dp,
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp
                    )
                    .onFocusChanged { focusState ->
                        clickedSearch = focusState.isFocused
                        if (focusState.isFocused) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                    .animatedBorder({ progress }, White, Color.Black),
                maxLines = 1,
                shape = MaterialTheme.shapes.extraLarge,
                placeholder = {
                    Text(
                        text = when (collectionId) {
                            Constants.COLLECTION_ID_FAV -> "Search your favorite quotes..."
                            Constants.COLLECTION_ID_CUSTOM -> "Search your custom quotes..."
                            else -> "Search in ${state.collectionName}..."
                        },
                        color = Color.Gray
                    )
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Grey) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        WhiteCancelIcon(onClick = {
                            viewModel.onEvent(CollectionDetailEvent.OnSearchQueryChanged(""))
                            clickedSearch = false
                            keyboardController?.hide()
                        })
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Grey,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedLeadingIconColor = White,
                    unfocusedLeadingIconColor = Color.Gray,
                )
            )

            if (state.quotes.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp)
                ) {
                    itemsIndexed(state.quotes) { index, quote ->
                        val showHeart = collectionId == Constants.COLLECTION_ID_FAV
                        val showEdit = collectionId == Constants.COLLECTION_ID_CUSTOM
                        val showDelete = collectionId != Constants.COLLECTION_ID_FAV 

                        LibraryQuoteItem(
                            quote = quote,
                            showHeart = showHeart,
                            showEdit = showEdit,
                            showDelete = showDelete,
                            onLikeClick = { viewModel.onEvent(CollectionDetailEvent.Like(quote)) },
                            onShareClick = {
                                navHost.currentBackStackEntry?.savedStateHandle?.set("quote", quote)
                                navHost.navigate(Screen.Share.route)
                            },
                            onDeleteClick = {
                                quoteToDelete = quote
                            },
                            onEditClick = {
                                if (collectionId == Constants.COLLECTION_ID_CUSTOM) {
                                    val customQuote = CustomQuote(
                                        id = quote.id ?: 0,
                                        quote = quote.quote,
                                        author = quote.author,
                                        createdAt = quote.updatedAt
                                    )
                                    navHost.currentBackStackEntry?.savedStateHandle?.set("quote_to_edit", customQuote)
                                    navHost.navigate(Screen.AddCustomQuote.route)
                                }
                            },
                            modifier = Modifier
                                .zIndex((state.quotes.size - index).toFloat())
                                .graphicsLayer {
                                    rotationZ = cardRotation * if (index % 2 == 0) 1 else -1
                                    translationY = (cardOffset * ((5f - (index + 1)) / 5f)).dp
                                        .roundToPx()
                                        .toFloat()
                                }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val emptyMessage = when (collectionId) {
                        Constants.COLLECTION_ID_FAV -> "Looks empty... \n\nImport data if you have one from settings!"
                        Constants.COLLECTION_ID_CUSTOM -> "No custom quotes yet.\nTap + to create one!\n\n Import data if you have one from settings!"
                        else -> "No quotes found in this collection."
                    }
                    Text(
                        text = emptyMessage,
                        color = White,
                        fontFamily = GIFont,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (quoteToDelete != null) {
            DeleteConfirmationDialog(
                title = if (collectionId == Constants.COLLECTION_ID_CUSTOM) "Delete Quote?" else "Remove Quote?",
                onConfirm = {
                    viewModel.onEvent(CollectionDetailEvent.Delete(quoteToDelete!!))
                    quoteToDelete = null
                },
                onDismiss = {
                    quoteToDelete = null
                }
            )
        }

        if (collectionId == Constants.COLLECTION_ID_CUSTOM) {
            FloatingActionButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    navHost.navigate(Screen.AddCustomQuote.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Custom Quote", tint = Color.Black)
            }
        }
        
        CustomIndicator(isRefreshing, pullRefreshState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomIndicator(isRefreshing: Boolean, pullRefreshState: PullToRefreshState) {
    val animatedOffset by animateDpAsState(
        targetValue = when {
            isRefreshing -> 200.dp
            pullRefreshState.distanceFraction in 0f..1f -> (pullRefreshState.distanceFraction * 200).dp
            pullRefreshState.distanceFraction > 1f -> (200 + (((pullRefreshState.distanceFraction - 1f) * .1f) * 200)).dp
            else -> 0.dp
        }, label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .offset(y = (-200).dp)
            .offset { IntOffset(0, animatedOffset.roundToPx()) }
    ) {
        WhiteBeam(pullRefreshState, isRefreshing)
        RainbowRays(isRefreshing, pullRefreshState)
        GlowingTriangle(pullRefreshState, isRefreshing)
    }
}
