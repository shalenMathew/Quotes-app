package com.shalenmathew.quotesapp.util

enum class NotificationMode {
    FREQUENCY,
    DAILY_TIME;

    companion object {
        fun fromName(name: String?): NotificationMode =
            entries.firstOrNull { it.name == name } ?: FREQUENCY
    }
}
