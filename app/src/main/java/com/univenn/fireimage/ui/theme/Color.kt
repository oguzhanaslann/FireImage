package com.univenn.fireimage.ui.theme

import androidx.compose.ui.graphics.Color

// Dark Theme Colors
val DarkPrimary = Color(0xFF2196F3)  // AccentBlue
val DarkOnPrimary = Color.White
val DarkPrimaryContainer = Color(0xFF0088FF)  // AccentLightBlue
val DarkOnPrimaryContainer = Color.White

val DarkSecondary = Color(0xFF625b71)
val DarkOnSecondary = Color.White
val DarkSecondaryContainer = Color(0xFF424242)  // SurfacePrimary
val DarkOnSecondaryContainer = Color.White

val DarkTertiary = Color(0xFF7D5260)
val DarkOnTertiary = Color.White

val DarkBackground = Color(0xFF0B0B0B)  // BackgroundPrimary
val DarkOnBackground = Color.White

val DarkSurface = Color(0xFF1A1A1A)  // BackgroundSecondary
val DarkOnSurface = Color.White
val DarkSurfaceVariant = Color(0xFF2A2A2A)  // BackgroundTertiary
val DarkOnSurfaceVariant = Color.Gray

val DarkOutline = Color(0xFF5A5A5A)  // BorderColor

// Light Theme Colors (keeping them same as dark for now since the app seems to be dark-themed)
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

val LightBackground = DarkBackground
val LightOnBackground = DarkOnBackground

val LightSurface = DarkSurface
val LightOnSurface = DarkOnSurface
val LightSurfaceVariant = DarkSurfaceVariant
val LightOnSurfaceVariant = DarkOnSurfaceVariant

val LightOutline = DarkOutline

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

// Legacy theme colors (keeping for backward compatibility)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)