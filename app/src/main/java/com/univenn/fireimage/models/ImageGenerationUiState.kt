package com.univenn.fireimage.models

import android.graphics.Bitmap

data class ImageGenerationUiState(
    val prompt: String = "",
    val bitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)  {
    fun withBitmap(bitmap: Bitmap) = copy(bitmap = bitmap)
    fun withPrompt(prompt: String) = copy(prompt = prompt)
    fun promptCleared() = copy(prompt = "")
    fun loading() = copy(isLoading = true)
    fun loaded() = copy(isLoading = false)
}