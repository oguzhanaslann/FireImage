package com.univenn.fireimage.models

import android.graphics.Bitmap

data class ImageGenerationUiState(
    val prompt: String = "",
    val bitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
) 