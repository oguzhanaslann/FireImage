package com.univenn.fireimage

sealed class Screen(val route: String) {
    data object ImageGeneration : Screen("image_generation")
    data object Chat : Screen("chat")
}