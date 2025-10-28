# Pull Request: Add Artisan Card theme (selectable + image background)

## Summary
Adds a new customizable quote theme called **Artisan Card**. The theme renders a rounded blue card on a light grey background and supports a user-selected image shown as the card's top banner. Users can select the theme from the Customize bottom sheet, preview it, pick a gallery image, set it as default, and download/share the rendered quote.

Closes #153

## What changed
- Added new `QuoteStyle` value:
  - `ArtisanCardTheme` in `app/src/main/java/.../share_screen/util.kt`
- Implemented the UI/composable:
  - `ArtisanCardStyle` in `app/src/main/java/.../share_screen/components/CustomQuotesThemes.kt`
    - Renders a grey screen background, rounded blue card, top image area (user-selectable), quote text and author, and a small decorative bar.
- Wired selection + preview + image picker:
  - `ShareScreen.kt` — added ThemeItem entry for "Artisan Card", render mapping for `ArtisanCardTheme`, and enabled the gallery picker icon when this theme is active.
- Persisted the theme:
  - `DefaultQuoteStylePreferencesImpl.kt` — added save/get mapping strings so the theme can be set as default and lasts across sessions.

## How to test (manual)
1. Build the app and install on an emulator/device:
   - assemble: `./gradlew assembleDebug`
   - install: `./gradlew installDebug` or `adb install -r app/build/outputs/apk/debug/app-debug.apk`
2. Open the app → Share screen.
3. Tap the Customize (bottom sheet) icon.
4. Select **Artisan Card** from the list — the preview area should update.
5. While Artisan Card is active, tap the gallery icon (toolbar) and pick an image from the device — it should appear as the top banner inside the rounded card, respecting rounded corners.
6. Tap Download and verify the saved image appears in the gallery (or file saved location).
7. Tap Share and verify the share intent opens with the captured image.
8. Set Artisan Card as default (via checkbox) → close and reopen Share screen → confirm the theme persisted.

## Automated checks to run (optional)
- Unit tests:
  - `./gradlew testDebugUnitTest`
- Lint:
  - `./gradlew lint`

## Screenshots
Attach screenshots to the PR:
- Card preview with selected image
- Bottom-sheet showing “Artisan Card” option and selected state
- Example of saved/downloaded image (capture result)

## Notes & follow-ups
- Visual tuning: used `Color(0xFF2077FF)` for the blue card and reasonable paddings/radii to match the reference. If you prefer an exact hex or font, provide the values and I can update the composable.
- The barcode-like decorative box is currently a simple black rounded rectangle placeholder — we can replace it with a proper drawable or generated barcode if desired.
- Crop/scale: user-selected images are shown with `ContentScale.Crop`. If you want a crop UI, I can add a simple cropping step before display.
- I pushed these changes to branch `feat/artisan-card-theme`.

## Risks / Rollback
- Low risk: changes are limited to the Share screen feature and the style persistence mapping.
- Rollback: revert the branch or revert the PR on merge if issues are found.

---

You can open the PR in the browser using this prefilled URL:
https://github.com/bhumikakadbe/Quotes-app/pull/new/feat/artisan-card-theme?title=Add%20Artisan%20Card%20theme%20(selectable%20%2B%20image%20background)%20%E2%80%94%20closes%20%23153

Copy the `PR_DESCRIPTION.md` contents into the PR body or attach this file when creating the PR.
