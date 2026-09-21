package com.shalenmathew.quotesapp.domain.usecases.widget

import com.shalenmathew.quotesapp.domain.repository.WidgetRepository
import javax.inject.Inject

class UpdateWidgetThemeUseCase @Inject constructor(
    private val widgetRepository: WidgetRepository
) {
    suspend operator fun invoke(themeId: String): Result<Unit> {
        return widgetRepository.updateWidgetTheme(themeId)
    }
}
