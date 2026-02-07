package com.shalenmathew.quotesapp.data.local

import com.shalenmathew.quotesapp.domain.repository.AnimationPreferences
import jakarta.inject.Inject

class AnimationPreferencesImpl @Inject constructor() : AnimationPreferences {

    @Volatile
    private var hasShown: Boolean = false

    override fun hasRainbowAnimationBeenShown(): Boolean {
        return hasShown
    }

    override fun setRainbowAnimationShown() {
        hasShown = true
    }

}
