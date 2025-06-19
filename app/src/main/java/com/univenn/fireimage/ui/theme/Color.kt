package com.univenn.fireimage.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors - Blue theme
val DarkPrimary = Color(0xFF2196F3)  // Main accent color for primary actions
val DarkOnPrimary = Color.White
val DarkPrimaryContainer = Color(0xFF0088FF)  // Used for containers and secondary actions
val DarkOnPrimaryContainer = Color.White

// Secondary Colors - Gray theme
val DarkSecondary = Color(0xFF625b71)  // Used for less prominent components
val DarkOnSecondary = Color.White
val DarkSecondaryContainer = Color(0xFF424242)  // Used for cards and elevated components
val DarkOnSecondaryContainer = Color.White

// Tertiary Colors - Purple theme
val DarkTertiary = Color(0xFF7D5260)  // Used for contrasting accents
val DarkOnTertiary = Color.White
val DarkTertiaryContainer = Color(0xFF5E3F4C)  // Used for contrasting containers
val DarkOnTertiaryContainer = Color.White

// Background & Surface Colors
val DarkBackground = Color(0xFF0B0B0B)  // Main background
val DarkOnBackground = Color.White

val DarkSurface = Color(0xFF1A1A1A)  // Surface elements like cards
val DarkOnSurface = Color.White
val DarkSurfaceVariant = Color(0xFF2A2A2A)  // Alternative surface color
val DarkOnSurfaceVariant = Color.Gray

// Outline
val DarkOutline = Color(0xFF5A5A5A)  // Used for borders and dividers
val DarkOutlineVariant = Color(0xFF3A3A3A)  // Subtle outline variant

// Error Colors
val DarkError = Color(0xFFCF6679)  // Used for error states
val DarkOnError = Color.White
val DarkErrorContainer = Color(0xFFB1384E)
val DarkOnErrorContainer = Color.White

// Light theme colors mirror dark theme for now
val LightPrimary = DarkPrimary
val LightOnPrimary = DarkOnPrimary
val LightPrimaryContainer = DarkPrimaryContainer
val LightOnPrimaryContainer = DarkOnPrimaryContainer

val LightSecondary = DarkSecondary
val LightOnSecondary = DarkOnSecondary
val LightSecondaryContainer = DarkSecondaryContainer
val LightOnSecondaryContainer = DarkOnSecondaryContainer

val LightTertiary = DarkTertiary
val LightOnTertiary = DarkOnTertiary
val LightTertiaryContainer = DarkTertiaryContainer
val LightOnTertiaryContainer = DarkOnTertiaryContainer

val LightBackground = DarkBackground
val LightOnBackground = DarkOnBackground

val LightSurface = DarkSurface
val LightOnSurface = DarkOnSurface
val LightSurfaceVariant = DarkSurfaceVariant
val LightOnSurfaceVariant = DarkOnSurfaceVariant

val LightOutline = DarkOutline
val LightOutlineVariant = DarkOutlineVariant

val LightError = DarkError
val LightOnError = DarkOnError
val LightErrorContainer = DarkErrorContainer
val LightOnErrorContainer = DarkOnErrorContainer

// Background colors
val BackgroundPrimary = DarkBackground
val BackgroundSecondary = DarkSurface
val BackgroundTertiary = DarkSurfaceVariant

// Accent colors
val AccentBlue = DarkPrimary
val AccentLightBlue = DarkPrimaryContainer

// Text colors
val TextPrimary = DarkOnPrimary
val TextSecondary = DarkOnSecondary

// Surface colors
val SurfacePrimary = DarkSecondaryContainer
val BorderColor = DarkOutline

// UI Element colors
val EditButtonBackground = DarkSurface.copy(alpha = 0.7f)
val MessageBubbleFromMe = AccentBlue
val MessageBubbleFromOther = SurfacePrimary
val PlaceholderText = DarkOnSurfaceVariant

// Legacy theme colors (keeping for backward compatibility)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)