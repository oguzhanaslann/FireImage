package com.univenn.fireimage

import android.graphics.Bitmap

data class ImageGenerationUiState(
    val bitmap: Bitmap? = null,
    val prompt: String = "",
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)