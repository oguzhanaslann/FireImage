package com.univenn.fireimage.screens.image_generation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univenn.fireimage.models.ImageGenerationUiState
import com.univenn.fireimage.utils.geminiImageGen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageGenerationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ImageGenerationUiState())
    val uiState: StateFlow<ImageGenerationUiState> = _uiState.asStateFlow()

    fun handleSelectedImage(bitmap: Bitmap) {
        _uiState.value = _uiState.value.withBitmap(bitmap)
    }

    fun generateImage() {
        val prompt = _uiState.value.prompt
        viewModelScope.launch {
            _uiState.value = _uiState.value.loading()
            val generatedBitmap = geminiImageGen(prompt)
            _uiState.value = _uiState.value
                .withBitmap(generatedBitmap)
                .promptCleared()
                .loaded()
        }
    }

    fun setPrompt(newPrompt: String) {
        _uiState.value = _uiState.value.withPrompt(newPrompt)
    }

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(
            isEditing = !_uiState.value.isEditing
        )
        .promptCleared()
    }

    fun editImage() {
        val currentState = _uiState.value
        val bitmap = currentState.bitmap ?: return
        val prompt = currentState.prompt
        
        viewModelScope.launch {
            _uiState.value = currentState.loading()
            val editedBitmap = com.univenn.fireimage.utils.editImage(prompt, bitmap) ?: return@launch
            _uiState.value = currentState
                .withBitmap(editedBitmap)
                .promptCleared()
                .loaded()
        }
    }
}