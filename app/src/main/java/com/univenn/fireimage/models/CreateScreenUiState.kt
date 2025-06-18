package com.univenn.fireimage.models

import android.graphics.Bitmap

data class CreateScreenUiState(
    val prompt: String = "",
    val image: Bitmap? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
) 