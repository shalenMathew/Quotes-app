package com.shalenmathew.quotesapp.presentation.screens.custom_quote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.util.CustomQuoteEvent
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.CustomQuoteViewModel
import kotlin.text.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomQuoteScreen(
    navHost: NavHostController,
    paddingValues: PaddingValues,
    viewModel: CustomQuoteViewModel = hiltViewModel(),

) {

    val quoteToEdit: CustomQuote? = navHost.previousBackStackEntry?.savedStateHandle?.get<CustomQuote>("quote_to_edit")
    val isEditMode = quoteToEdit != null

    var quoteText by remember { mutableStateOf(quoteToEdit?.quote ?: "") }
    var authorText by remember { mutableStateOf(quoteToEdit?.author ?: "") }
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Edit Custom Quote" else "Create Custom Quote",
                        fontFamily = GIFont
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        navHost.previousBackStackEntry?.savedStateHandle?.remove<CustomQuote>("quote_to_edit")
                        navHost.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (quoteText.isNotBlank()) {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (isEditMode) {
                            //update
                            viewModel.onEvent(
                                CustomQuoteEvent.UpdateQuote(
                                    quote = quoteToEdit.copy(
                                        quote = quoteText.trim(),
                                        author = if (authorText.isNotBlank()) authorText.trim() else "Unknown"
                                    )
                                )
                            )

                        } else {

                            //save
                            viewModel.onEvent(
                                CustomQuoteEvent.SaveQuote(
                                    quote = quoteText.trim(),
                                    author = authorText.trim()
                                )
                            )
                        }

                        navHost.previousBackStackEntry?.savedStateHandle?.remove<CustomQuote>("quote_to_edit")
                        navHost.popBackStack()
                    }
                },
                containerColor = Color.White,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = if (isEditMode) "Update Quote" else "Save Quote",
                    tint = Color.Black
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.Black

    ){ innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            OutlinedTextField(
                value = quoteText,
                onValueChange = { quoteText = it },
                label = { Text("Quote *", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                maxLines = 8
            )

            OutlinedTextField(
                value = authorText,
                onValueChange = { authorText = it },
                label = { Text("Author (Optional)", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                placeholder = { Text("Unknown", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
            )

            Text(
                "* Required field",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }

        BackHandler {
            navHost.previousBackStackEntry?.savedStateHandle?.remove<CustomQuote>("quote_to_edit")
            navHost.popBackStack()
        }

    }
