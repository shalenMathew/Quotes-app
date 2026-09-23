package com.shalenmathew.quotesapp.presentation.screens.settings_screen.troubleshoot.component

data class TroubleshootItem(
    val question: String,
    val answer: String
)

data class TroubleshootCategory(
    val title: String,
    val items: List<TroubleshootItem>
)

val troubleshootCategories = listOf(
    TroubleshootCategory(
        title = "Widget Setup & Issues",
        items = listOf(
            TroubleshootItem("How to add the widget?", "Long press on the Quotes App icon on your home screen and select Widgets."),
            TroubleshootItem("Why isn't the widget refreshing?", "Android limits background widget updates to save battery. Ensure the app is excluded from 'Battery Optimization' in your device settings."),
            TroubleshootItem("Where does the widget pull quotes from?", "The widget can pull quotes from three sources: online, your favorites, or your custom entries. You can choose your preferred source in Settings."),
            TroubleshootItem("My widget is acting funny?", "If your widget stops updating or seems frozen, try rebooting your device. Android OS occasionally pauses background widget tasks.")
        )
    ),
    TroubleshootCategory(
        title = "Notifications",
        items = listOf(
            TroubleshootItem("Notifications are not appearing", "Please ensure notification permissions are granted and the app is not 'Battery Optimized' in system settings."),
            TroubleshootItem("About Notification Scheduling?", "You can schedule notifications to trigger either every couple of hours or once a day at your chosen time.")
        )
    ),
    TroubleshootCategory(
        title = "App Features & Data",
        items = listOf(
            TroubleshootItem("What is 'Custom Quote'?", "A feature that allows you to create, save, and manage your own custom quotes."),
            TroubleshootItem("About Backup & Restore data?", "You can export and import your app data. If you change the exported file name, make sure it ends with '.json' so the import works correctly.")
        )
    ),
    TroubleshootCategory(
        title = "General & About",
        items = listOf(
            TroubleshootItem("Why is the app so good?", "Cause I made it 🗣️"),
            TroubleshootItem("Why did you make the app?", "Cause the app is so good 🗣️")
        )
    )
)