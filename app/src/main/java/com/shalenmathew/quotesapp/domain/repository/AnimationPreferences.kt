package com.shalenmathew.quotesapp.domain.repository

interface AnimationPreferences {
    fun hasRainbowAnimationBeenShown(): Boolean
    fun setRainbowAnimationShown()
}
