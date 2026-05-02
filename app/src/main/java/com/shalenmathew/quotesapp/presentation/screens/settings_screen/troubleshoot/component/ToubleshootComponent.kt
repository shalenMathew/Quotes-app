package com.shalenmathew.quotesapp.presentation.screens.settings_screen.troubleshoot.component

data class TroubleshootItem(
    val question: String,
    val answer: String,

)

val troubleshootQuestions = listOf(
    TroubleshootItem("Notifications are not appearing", "Check if notification permissions are granted and  ensure the app is not 'Battery Optimized' in app system settings."),
    TroubleshootItem("Why isn't the widget refreshing?", "Android limits widget updates to save battery. Ensure the app is not 'Battery Optimized' in app system settings."),
    TroubleshootItem("How to add the widget?", "Long press on your home screen, select 'Widgets', find Quotes App, and drag it to your screen."),
    TroubleshootItem("From where do the widget pull quotes from?", "The widget can pull quotes from three sources: online, your favorites, or your own custom entries. You can easily choose your preferred source in the settings."),
    TroubleshootItem("what is 'Custom Quote'? ", "Its a feature that allows you to create and save your own quotes."),
    TroubleshootItem("what is the Prism Rainbow? ", "Its a prism rainbow \uD83D\uDDE3\uFE0F"),
)