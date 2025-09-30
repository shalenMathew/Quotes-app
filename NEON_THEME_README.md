# Neon Theme - New Feature

## Overview
Added a new **Neon Theme** to the Quotes app as requested in issue #107. This theme provides a futuristic, cyberpunk-inspired design with vibrant neon colors and glowing effects.

## Features
- **Dark Background**: Deep black with subtle gradient for depth
- **Neon Colors**: Cyan, Pink, and Blue neon accents
- **Gradient Border**: Animated neon border effect
- **Decorative Elements**: Small neon dots in corners for visual appeal
- **Typography**: Uses the app's existing font family with neon color scheme

## Implementation Details

### Files Modified:
1. `Color.kt` - Added neon color definitions
2. `util.kt` - Added NeonTheme to QuoteStyle enum
3. `CustomQuotesThemes.kt` - Implemented NeonThemeScreen composable
4. `CaptureBitmap.kt` - Added NeonTheme case to theme selection
5. `ShareScreen.kt` - Added Neon Theme option to the theme picker

### Color Palette:
- **Neon Blue**: `#00FFFF` (Cyan)
- **Neon Pink**: `#FF00FF` (Magenta) 
- **Neon Cyan**: `#00E5FF` (Light Cyan)
- **Dark Neon**: `#0A0A0A` (Very Dark Gray)

### Theme Structure:
- Outer box with gradient background and neon border
- Inner content area with subtle transparency
- Quote text in neon cyan
- Author text in neon pink
- App name in neon blue
- Decorative neon dots in corners

## Usage
Users can now select the "Neon Theme" from the theme picker in the share screen. The theme will be applied to quotes when sharing or saving them.

## Hacktoberfest Contribution
This implementation resolves issue #107 and adds a modern, visually appealing theme option to the Quotes app.
