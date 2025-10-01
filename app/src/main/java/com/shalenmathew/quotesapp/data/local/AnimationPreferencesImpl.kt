package com.shalenmathew.quotesapp.data.local

import android.content.SharedPreferences
import com.shalenmathew.quotesapp.domain.repository.AnimationPreferences
import androidx.core.content.edit
import jakarta.inject.Inject

class AnimationPreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): AnimationPreferences {

    override fun hasRainbowAnimationBeenShown(): Boolean {
        return sharedPreferences.getBoolean(RAINBOW_ANIMATION_SHOWN_KEY, false)
    }

    override fun setRainbowAnimationShown() {
        sharedPreferences.edit {
            putBoolean(RAINBOW_ANIMATION_SHOWN_KEY, true)
        }
    }

    companion object {
        private const val RAINBOW_ANIMATION_SHOWN_KEY = "rainbow_animation_shown_key"
    }
}
