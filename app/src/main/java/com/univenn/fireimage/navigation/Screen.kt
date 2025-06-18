package com.univenn.fireimage.navigation

sealed class Screen(val route: String) {
    object ImageGeneration : Screen("image_generation")
    object Chat : Screen("chat")
} 