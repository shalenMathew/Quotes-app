package com.shalenmathew.quotesapp.presentation.screens.home_screen.util

import com.shalenmathew.quotesapp.domain.model.Quote

data class QuoteState (
    // state doesn't recommend using mutable list coz it gives us function like .copy(), .add(), etc...
    // and when u change the list using .copy(), it only changes the internal part of the list without changing the reference
    // where as list which is immutable and need a new object to trigger a recomposition ,

    val dataList:MutableList<Quote> = mutableListOf(),
    val qot:Quote? = null,
    val liked:Boolean=false,
    val error:String="",
    val isLoading: Boolean =false,
    )