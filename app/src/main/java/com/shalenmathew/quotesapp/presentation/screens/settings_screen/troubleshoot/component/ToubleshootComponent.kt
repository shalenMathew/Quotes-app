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
    TroubleshootItem("About Backup & Restore data? ", "You can import & export ur data , but make sure while ur exporting and trying to change name, the name should end with '.json' or the import wont work "),
    TroubleshootItem("About Notification Scheduling? ", "You can only choose to schedule between 2 options , either the app can notify u every couple of hours or just every day on the scheduled time"),
    TroubleshootItem("what is 'Custom Quote'? ", "Its a feature that allows you to create and save your own quotes."),
    TroubleshootItem("why the apps so good? ", "cause i made it \uD83D\uDDE3\uFE0F"),
    TroubleshootItem("why did u made the app? ", "cause the apps so good \uD83D\uDDE3\uFE0F"),
)